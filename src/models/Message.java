package models;

import exceptions.DBConnectionException;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Message extends Model{

    private User recipient;
    private User sender;
    private Timestamp sentTime;
    private String description;
    private boolean isInvitation;
    private boolean hasBeenRead;

    public static Message getByID(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        Message message;
        if(mc.contains(Message.class, id)) message = mc.get(Message.class, id);
        else message = new Message();
        ResultSet results = db.query("" +
                "SELECT RecipientID, SenderID, SentTime, Description, IsInvitation, HasBeenRead\n" +
                "FROM MESSAGE\n" +
                "WHERE MessageID = " + id);
        if(!results.next()) throw new SQLException("No Message with that id");
        message.recipient = User.getByID(results.getInt("RecipientID"), db, mc);
        message.sender = User.getByID(results.getInt("SenderID"), db, mc);
        message.sentTime = results.getTimestamp("SentTime");
        message.description = results.getString("Description");
        message.isInvitation = results.getBoolean("IsInvitation");
        message.hasBeenRead = results.getBoolean("HasBeenRead");
        if(results.next()) throw new SQLException("Result not unique");
        mc.put(id, message);
        return message;
    }

}
