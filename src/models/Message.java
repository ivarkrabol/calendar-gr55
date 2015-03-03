package models;

import exceptions.DBConnectionException;
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

    public int getId() {
        return id;
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

    public static Message getById(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        Message message;
        if(mc.contains(Message.class, id)) message = mc.get(Message.class, id);
        else message = new Message();
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

        db.query(sql);
    }
}
