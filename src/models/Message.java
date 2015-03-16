package models;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Message extends Model{

    private int id;
    private User recipient;
    private User sender;
    private Timestamp sentTime;
    private String description;
    private boolean invitation;
    private boolean read;
//
//    private SimpleStringProperty sender;
//    private SimpleStringProperty invitation;
//    private StringProperty descriptionProperty = new SimpleStringProperty();
//
//    private Message(String description, User sender, boolean invitation);
//
//
//
//    private Property<Message> messageProperty =  new ObjectPropertyBase<Message>(null) {
//
//        @Override
//        public Object getBean() {
//            return this;
//        }
//
//        @Override
//        public String getName() {
//            return "message";
//        }
//    };


    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInvitation() {
        return invitation;
    }

    public void setInvitation(boolean invitation) {
        this.invitation = invitation;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }


    //should move this method into User?
    public static ObservableList<Message> getInbox(int UserID, DB db, ModelCache mc) throws SQLException, DBConnectionException  {
        ResultSet rs;
        ObservableList<Message> inbox = FXCollections.observableArrayList();
        rs = db.query("SELECT MessageID FROM MESSAGE WHERE RecipientID = " + UserID);
        while (rs.next()) {
            int temp = rs.getInt("MessageID");
            inbox.add(getById(temp, db, mc));
        }
        return inbox;
    }


    public static Message getById(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        Message message;
        if(mc.contains(Message.class, id)) message = mc.get(Message.class, id);
        else message = new Message();
        message.setId(id);
        message.refreshFromDB(db, mc);
        mc.put(id, message);
        return message;
    }




    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "" +
                "SELECT RecipientID, SenderID, SentTime, Description, IsInvitation, HasBeenRead\n" +
                "FROM MESSAGE\n" +
                "WHERE MessageID = " + id;

        ResultSet results = db.query(sql);
        if(!results.next()) throw new SQLException("No Message with that ID");
        setRecipient(User.getById(results.getInt("RecipientID"), db, mc));
        setSender(User.getById(results.getInt("SenderID"), db, mc));
        setSentTime(results.getTimestamp("SentTime"));
        setDescription(results.getString("Description"));
        setInvitation(results.getBoolean("IsInvitation"));
        setRead(results.getBoolean("HasBeenRead"));
        if(results.next()) throw new SQLException("Result not unique");

    }

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {
        String sql = "UPDATE MESSAGE\n" +
                "RecipientID = " + getRecipient().getId() + ",\n" +
                "SenderID = " + getSender().getId() + ",\n" +
                "SentTime = '" + getSentTime() + "',\n" +
                "Description = '" + getDescription() + "',\n" +
                "IsInvitation = '" + isInvitation() + "',\n" +
                "HasBeenRead = '" + isRead() + "'\n" +
                "WHERE MessageID = " + getId();

        db.update(sql);
    }

    @Override
    public void insertToDB(DB db) throws SQLException, DBConnectionException {
        String updateSql = "INSERT INTO USER\n" +
                "(RecipientID, SenderID, SentTime, Description, IsInvitation, HasBeenRead)\n" +
                "VALUES (\n" +
                getRecipient().getId() + ",\n" +
                getSender().getId() + ",\n" +
                "'" + getSentTime() + "',\n" +
                "'" + getDescription() + "',\n" +
                String.valueOf(isInvitation()) + ",\n" +
                String.valueOf(isRead()) + ")";
        db.update(updateSql);
        String querySql = "SELECT MAX(MessageID) AS ID FROM MESSAGE";

        ResultSet results = db.query(querySql);
        if (!results.next()) throw new SQLException("This shouldn't happen. Sooo...");
        setId(results.getInt("ID"));
    }
}
