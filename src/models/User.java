package models;

import exceptions.DBConnectionException;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Model{

    private int ID;
    private String email;
    private String lastName;
    private String firstName;
    private String phoneNr;

    public User() {

    }

    private User(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public static User getByID(int ID, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        User user;
        if(mc.contains(User.class, ID)) user = mc.get(User.class, ID);
        else user = new User(ID);
        mc.put(ID, user);
        user.refresh(db, mc);
        return user;
    }

    @Override
    public void refresh(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "" +
                "SELECT EMail, LastName, FirstName, PhoneNr\n" +
                "FROM USER\n" +
                "WHERE UserID = " + ID;

        ResultSet results = db.query(sql);
        if (!results.next()) throw new SQLException("No User with that ID in database");
        setEmail(results.getString("EMail"));
        setLastName(results.getString("LastName"));
        setFirstName(results.getString("FirstName"));
        setPhoneNr(results.getString("PhoneNr"));
        if(results.next()) throw new SQLException("Result not unique");
    }

    @Override
    public void save(DB db) throws SQLException, DBConnectionException {
        String sql = "UPDATE USER " +
                "EMail = '" + getEmail() + "', " +
                "LastName = '" + getLastName() + "', " +
                "FirstName = '" + getFirstName() + "', " +
                "PhoneNr = '" + getPhoneNr() + "' " +
                "WHERE UserID = " + getID();
        db.query(sql);
    }
}
