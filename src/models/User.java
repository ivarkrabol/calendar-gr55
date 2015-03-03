package models;

import exceptions.DBConnectionException;
import util.DB;
import util.ModelCache;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Model{

    private String email;
    private String lastName;
    private String firstName;
    private String phoneNr;

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

    public static User getByID(int id, DB db, ModelCache mc) throws SQLException, DBConnectionException {
        User user;
        if(mc.contains(User.class, id)) user = mc.get(User.class, id);
        else user = new User();
        ResultSet results = db.query("" +
                "SELECT EMail, LastName, FirstName, PhoneNr\n" +
                "FROM USER\n" +
                "WHERE UserId = " + id);
        if(!results.next()) throw new SQLException("No User with that id in database");
        user.email = results.getString("EMail");
        user.lastName = results.getString("LastName");
        user.firstName = results.getString("FirstName");
        user.phoneNr = results.getString("PhoneNr");
        if(results.next()) throw new SQLException("Result not unique");
        mc.put(id, user);
        return user;
    }
    public String getName() {
        return getFirstName() + " " +getLastName();
    }

    public void setName(String name) {
        // First name or last name?
    }
}
