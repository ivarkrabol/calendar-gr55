package models;

import exceptions.DBConnectionException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Group extends Model {

    private int id;
    //private String groupName;
    //private StringProperty nummer = new SimpleStringProperty();
    
//    public StringProperty nummerProperty(){
//    	return nummer;
//    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addMember(User user, DB db, ModelCache mc) throws SQLException, DBConnectionException { //should this also include an Appointment ID function
        String sql = "UPDATE PARTICIPANTS\n" +
                "UserID = '" + user.getId() + "',\n" +
                "GroupID = '" + getId() + "',\n" +
                "WHERE GroupID = " + getId();
        db.query(sql);
    }


    public static ObservableList<Group> getGroupsUserIsPartOf(int UserID, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        ResultSet rs;
        ObservableList<Group> groups = FXCollections.observableArrayList();
        rs = db.query("SELECT GroupID FROM PARTICIPANTS WHERE UserID = " + UserID + " AND GroupID is not NULL");
        while (rs.next()) {
            int temp = rs.getInt("GroupID");
            groups.add(getById(temp, db, mc));
        }
        return groups;

    }
    public static Group getById(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException { // this isn't done
        Group group;
        if (mc.contains(Group.class, id)) group = mc.get(Group.class, id);
        else {
            group = new Group();
        }
        group.setId(id);
        mc.put(id, group);
        group.refreshFromDB(db, mc);
        return group;
    }
    
    public static String getName(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException  {
        ResultSet results = db.query("SELECT GroupName FROM `GROUP` WHERE GroupID = " + id);
        String groupName = null;
        while (results.next()) {
        	groupName = results.getString("GroupName");
        }
        return groupName;
    }


    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException { // ikke ferdig
        String sql = "" +
                "SELECT 'Description', 'AdministratorID'\n" +
                "FROM `GROUP` WHERE GroupID = " + id;
        ResultSet results = db.query(sql);
        if (!results.next()) throw new SQLException("No Group with that ID in database");
        if (results.next()) throw new SQLException("Result not unique");

    }

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {

    }

    @Override
    public void insertToDB(DB db) throws SQLException, DBConnectionException {

    }
}

