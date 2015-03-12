package models;

import exceptions.DBConnectionException;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Model{

    private int id;
    private String email;
    private String lastName;
    private String firstName;
    private String phoneNr;
    private Calendar calendar;
    private ObservableList<User> searchResults;


    public User() {

    }

    private User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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


    public Calendar getCalendar() {
        return calendar;
    }

    public final void setCalendar(DB db, ModelCache modelCache){
         calendar = new Calendar(this.getId(), db, modelCache, "UserID");

    }

    public ObservableList<User> searchForUser(String UserName, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        ResultSet rs;
        rs = db.query("SELECT UserID FROM USER WHERE FirstName = " + UserName + "OR LastName = " + UserName + "OR Email = " + UserName);
        while (rs.next()) {
            int temp = rs.getInt("UserID");
            searchResults.add(getById(temp, db, mc));
        }
        return searchResults; // return a list of user that match the search and displays their fname, lname, tlf and email.
    }



    public static User getById(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        User user;
        if(mc.contains(User.class, id)) user = mc.get(User.class, id);
        else user = new User(id);
        mc.put(id, user);
        user.refreshFromDB(db, mc);
        return user;
    }

    @Override
    public void refreshFromDB(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        String sql = "" +
                "SELECT EMail, LastName, FirstName, PhoneNr\n" +
                "FROM USER\n" +
                "WHERE UserID = " + id;

        ResultSet results = db.query(sql);
        if (!results.next()) throw new SQLException("No User with that ID in database");
        setEmail(results.getString("EMail"));
        setLastName(results.getString("LastName"));
        setFirstName(results.getString("FirstName"));
        setPhoneNr(results.getString("PhoneNr"));
        if(results.next()) throw new SQLException("Result not unique");
    }

    @Override
    public void saveToDB(DB db) throws SQLException, DBConnectionException {
        String sql = "UPDATE USER\n" +
                "EMail = '" + getEmail() + "',\n" +
                "LastName = '" + getLastName() + "',\n" +
                "FirstName = '" + getFirstName() + "',\n" +
                "PhoneNr = '" + getPhoneNr() + "'\n" +
                "WHERE UserID = " + getId();
        db.query(sql);
    }
}
