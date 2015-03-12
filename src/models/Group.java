package models;

import exceptions.DBConnectionException;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Group extends Model{

    private int id;
    private ObservableList<Group> groups;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public ObservableList<Group> displayGroupsThatUserIsPartOf(int UserID, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        ResultSet rs;
        rs = db.query("SELECT GroupID FROM PARTICIPANTS WHERE UserID = " + UserID);
        while (rs.next()) {
            int temp = rs.getInt("GroupID");
            groups.add(getById(temp, db, mc));
        }
        return groups;

    }

    public static Group getById(int id, DB db, ModelCache mc ) throws SQLException, DBConnectionException { // this isn't done
        Group group;
        if(mc.contains(Group.class, id)) group = mc.get(Group.class, id);
        else {
            group = new Group();
        }
        mc.put(id, group);
        group.refreshFromDB(db, mc);
        return group;
    }


    @Override
<<<<<<< Updated upstream
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {

=======
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException { // ikke ferdig
        String sql = "" +
                "SELECT Description, Administrator\n" +
                "FROM GROUP\n" +
                "WHERE GroupID = " + id;

        ResultSet results = db.query(sql);
        if (!results.next()) throw new SQLException("No Group with that ID in database");
        if(results.next()) throw new SQLException("Result not unique");
>>>>>>> Stashed changes
    }

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {

    }
}
