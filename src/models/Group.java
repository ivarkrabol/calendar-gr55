package models;

import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Group extends Attendable {

    private int id;
    private String name;
    private String description;
    private Group parent;
    
    private Group() {
        super();
    }

    public Group(String name, String description){
        super();
        this.setName(name);
        this.setDescription(description);
    }
    
    private String getDescription(){
    	return description;
    }

    private void setDescription(String description) {
    	this.description = description;
	}

    public String getName(){
    	return name;
    }

	public void setName(String gname) {
		this.name = gname;
	}

    public int getId() {
        return id;
    }

	private void setId(int id) {
        this.id = id;
    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }
    //    USES 'invite(User user)' (inherited from Attendable class) in stead
//    public void addMember(User user, DB db, ModelCache mc) throws SQLException, DBConnectionException { //should this also include an Appointment ID function
//        String sql = "UPDATE PARTICIPANTS SET\n" +
//                "UserID = '" + user.getId() + "',\n" +
//                "GroupID = '" + getId() + "',\n" +
//                "WHERE GroupID = " + getId();
//        db.query(sql);
//    }


    public static ObservableList<Group> getGroupsByUser(int UserID, DB db, ModelCache mc) throws SQLException, DBConnectionException {
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
        else group = new Group();
        group.setId(id);
        mc.put(id, group);
        group.refreshFromDB(db, mc);
        return group;
    }

    @Override
    protected String getInvitationText() {
        return "You have been invited to a group by " + administrator.getEmail() + "\n" +
                "Name: " + getName() + "\n" +
                "Description: " + getDescription() + "\n" +
                (parent == null ? "\n" : "Parent group: " + parent.getName() + "\n\n") +
                "Use the button below to choose whether or not you wish to join.";
    }

    @Override
    protected String[] getIdPair() {
        return new String[] {"GroupID", ""+getId()};
    }

    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "" +
                "SELECT `GroupName`, `Description`, `ParentGroupId`, `AdministratorID`\n" +
                "FROM `GROUP`\n" +
                "WHERE GroupID = " + id;

        ResultSet results = db.query(sql);
        if (!results.next()) throw new SQLException("No Group with that ID in database");
        setName(results.getString("GroupName"));
        setDescription(results.getString("Description"));
        int parentId = results.getInt("ParentGroupID");
        if (!results.wasNull()) setParent(Group.getById(parentId, db, mc));
        setAdministrator(User.getById(results.getInt("AdministratorID"), db, mc));
        if (results.next()) throw new SQLException("Result not unique");

    }

    @Override
    public void saveToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "UPDATE `GROUP` SET\n" +
                "`GroupName` = '" + getName() + "',\n" +
                "`Description` = '" + getDescription() + "',\n" +
                (getParent() == null ? "" : "`ParentGroupID` = " + getParent().getId() + ",\n") +
                "`AdministratorID` = " + getAdministrator().getId() + ",\n" +
                "WHERE UserID = " + getId();
        db.update(sql);

        refreshFromDB(db, mc);
    }

    @Override
    public void insertToDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String updateSql = "INSERT INTO `GROUP`\n" +
                "(`GroupName`, `Description`, `ParentGroupID`, `AdministratorID`)\n" +
                "VALUES (\n" +
                "'" + getName() + "',\n" +
                "'" + getDescription() + "',\n" +
                getParent().getId() + ",\n" +
                getAdministrator().getId() + " );";
        db.update(updateSql);
        String querySql = "SELECT MAX(GroupID) AS ID FROM GROUP";
        ResultSet results = db.query(querySql);
        if (!results.next()) throw new SQLException("This shouldn't happen. Sooo...");
        setId(results.getInt("ID"));

        refreshFromDB(db, mc);
    }
}

