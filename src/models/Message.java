package models;

import exceptions.DBConnectionException;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.Callable;

public class Message extends Model{

//    public enum AttendableType {
//        APPOINTMENT ("AppointmentID"){@Override public Attendable getById(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {return Appointment.getById(id, db, mc);}},
//        GROUP ("GroupID"){@Override public Attendable getById(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {return Group.getById(id, db, mc);}};
//        private String idFieldName;
//        private AttendableType(String idFieldName) {this.idFieldName = idFieldName;}
//        public Attendable getById(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {return null;}
//        @Override public String toString() {return idFieldName;}
//    }

    private int id;
    private User recipient;
    private User sender;
    private boolean invitation;
    private boolean read;
    private Attendable attendable;
//    private AttendableType attendableType;

    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty description = new SimpleStringProperty();
    private SimpleBooleanProperty hasBeenRead = new SimpleBooleanProperty();
    private Property<Timestamp> sentTime =  new ObjectPropertyBase<Timestamp>(null) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "room";
        }
    };

    private Message() {

    }

    public Message(User recipient, User sender, boolean invitation, Attendable attendable) {
        this.recipient = recipient;
        this.sender = sender;
        this.invitation = invitation;
        this.attendable = attendable;
    }

    public Message(User recipient, User sender, String description, boolean invitation, Attendable attendable) {
        this.recipient = recipient;
        this.sender = sender;
        this.setDescription(description);
        this.invitation = invitation;
        this.attendable = attendable;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getSender() {
        return sender;
    }

    public void setUsername() {
        this.username.set(getSender().getFirstName()+" "+getSender().getLastName());
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        setUsername();
        return username;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime.setValue(sentTime);
    }

    public Timestamp getSentTime() {
        return sentTime.getValue();
    }

    public Property<Timestamp> sentTimeProperty() {
        return sentTime;
    }

    public SimpleBooleanProperty hasBeenReadProperty() {
        return hasBeenRead;
    }

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


    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public boolean isInvitation() {
        return invitation;
    }

    public void setInvitation(boolean invitation) {
        this.invitation = invitation;
    }

    public boolean isRead() {
        return hasBeenRead.get();
    }

    public void setRead(boolean read) {
        this.hasBeenRead.set(read);
    }

    public Attendable getAttendable() {
        return this.attendable;
    }

    public void setAttendable(Attendable attendable) {
        this.attendable = attendable;
    }

//    public AttendableType getAttendableType() {
//        return attendableType;
//    }
//
//    public void setAttendableType(AttendableType attendableType) {
//        this.attendableType = attendableType;
//    }

    //should move this method into User? Ivar says no (maybe rename this, and create method in User calling this method)
    public static ObservableList<Message> getInbox(int UserID, DB db, ModelCache mc) throws SQLException, DBConnectionException  {
        ResultSet rs;
        ObservableList<Message> inbox = FXCollections.observableArrayList();
        rs = db.query("SELECT MessageID FROM MESSAGE WHERE RecipientID = " + UserID);
        while (rs.next()) {
            int messageId = rs.getInt("MessageID");
            inbox.add(getById(messageId, db, mc));
        }
        return inbox;
    }


    public static void deleteMessage(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        db.update("DELETE FROM MESSAGE WHERE MessageID =" + id);
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
                "SELECT RecipientID, SenderID, SentTime, Description, IsInvitation, HasBeenRead, GroupID, AppointmentID\n" +
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
        int groupId = results.getInt("GroupID");
        if(results.wasNull()) {
//            setAttendableType(AttendableType.APPOINTMENT);
            System.out.println(results.getInt("AppointmentID"));
            setAttendable(Appointment.getById(results.getInt("AppointmentID"), db, mc));
        } else {
//            setAttendableType(AttendableType.GROUP);
            setAttendable(Group.getById(groupId, db, mc));
        }
        if(results.next()) throw new SQLException("Result not unique");

    }



    @Override
    public void saveToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "UPDATE MESSAGE SET\n" +
                "RecipientID = " + getRecipient().getId() + ",\n" +
                "SenderID = " + getSender().getId() + ",\n" +
                "Description = '" + getDescription() + "',\n" +
                "IsInvitation = '" + (isInvitation() ? 1 : 0) + "',\n" +
                "HasBeenRead = " + (isRead() ? 1 : 0) + ",\n" +
                getAttendable().getIdPair()[0] + " = " + getAttendable().getIdPair()[1] + "\n" +
                "WHERE MessageID = " + getId();

        db.update(sql);

        refreshFromDB(db, mc);
    }

    @Override
    public void insertToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String updateSql = "INSERT INTO MESSAGE\n" +
                "(RecipientID, SenderID, SentTime, Description, IsInvitation, " + getAttendable().getIdPair()[0] + ")\n" +
                "VALUES (\n" +
                getRecipient().getId() + ",\n" +
                getSender().getId() + ",\n" +
                "NOW(),\n" +
                "'" + getDescription() + "',\n" +
                String.valueOf(isInvitation()) + ",\n" +
                "" + getAttendable().getIdPair()[1] + ")";
        System.out.println(updateSql);
        db.update(updateSql);
        String querySql = "SELECT MAX(MessageID) AS ID FROM MESSAGE";

        ResultSet results = db.query(querySql);
        if (!results.next()) throw new SQLException("This shouldn't happen. Sooo...");
        setId(results.getInt("ID"));

        refreshFromDB(db, mc);
    }
}
