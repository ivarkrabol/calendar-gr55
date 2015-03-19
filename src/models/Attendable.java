package models;

import exceptions.AlreadyInvitedException;
import exceptions.DBConnectionException;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Attendable extends Model {

    public enum Response {
        NOT_ANSWERED ("NotAnswered"), HAS_ACCEPTED ("HasAccepted"), HAS_DECLINED ("HasDeclined");
        private String name;
        private Response(String name) { this.name = name;}
        @Override public String toString() { return name; }
    }

    protected User administrator;
    private Map<User, Response> responses;

    public Attendable() {
        responses = new HashMap<>();
    }

    public User getAdministrator() {
        return administrator;
    }

    public void setAdministrator(User administrator) {
        this.administrator = administrator;
        responses.put(administrator, Response.HAS_ACCEPTED);
    }

    public Map<User, Response> getResponses() {
        return new HashMap<User, Response>(responses);
    }

    public List<User> getByResponse(Response response) {
        List<User> byResponse = new ArrayList<>();
        for (User user : responses.keySet()) if (responses.get(user) == response) byResponse.add(user);
        return byResponse;
    }

    public void acceptInvite(User user) {
        responses.put(user, Response.HAS_ACCEPTED);
    }

    public void declineInvite(User user) {
        responses.put(user, Response.HAS_DECLINED);
    }

    public Message invite(User user) throws AlreadyInvitedException {
        if(responses.containsKey(user)) throw new AlreadyInvitedException();
        responses.put(user, Response.NOT_ANSWERED);
        Message invitation = new Message(user, administrator, true);
        System.out.println(invitation.getSender().getId());
        invitation.setDescription(getInvitationText());
        return invitation;
    }

    protected abstract String getInvitationText();

    protected abstract String[] getIdPair();

    private String getParticipantsWhereClause() {
        return "`" + getIdPair()[0] + "` = " + getIdPair()[1];
    }

    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        responses = new HashMap<>();
        String sql = "" +
                "SELECT `UserID`, `Response`\n" +
                "FROM `PARTICIPANTS`\n" +
                "WHERE " + getParticipantsWhereClause();
        ResultSet results = db.query(sql);
        while(results.next()) {
            Response response;
            switch(results.getString("Response")) {
                case "HasAccepted": response = Response.HAS_ACCEPTED; break;
                case "HasDeclined": response = Response.HAS_DECLINED; break;
                default: response = Response.NOT_ANSWERED;
            }
            responses.put(User.getById(results.getInt("UserID"), db, mc), response);
        }
    }

    @Override
    public void saveToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        updateParticipantsDB(db);
    }

    @Override
    public void insertToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        updateParticipantsDB(db);
    }

    private void updateParticipantsDB(DB db) throws SQLException, DBConnectionException {
        if(responses.isEmpty()) return;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO `PARTICIPANTS`\n");
        sql.append("(`UserID`, `" + getIdPair()[0] + "`, `Response`)\n");
        sql.append("VALUES\n");
        for (User user : responses.keySet()) {
            sql.append("(" + user.getId() + ", ");
            sql.append(getIdPair()[1] + ", '");
            sql.append(responses.get(user) + "'),\n");
        }
        sql.setLength(sql.length()-2);
        sql.append("\n");
        sql.append("ON DUPLICATE KEY UPDATE\n");
        sql.append("`Response` = VALUES(`Response`);");
        db.update(sql.toString());
    }
}
