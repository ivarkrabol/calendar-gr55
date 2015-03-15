package models;

import exceptions.DBConnectionException;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
    private int phoneNr;
    private String password;
    private Calendar calendar;


    public User() {
    }
    public User(String email, String lastName, String firstName, String phoneNr, String password){
        this.setEmail(email);
        this.setLastName(lastName);
        this.setFirstName(firstName);
        this.setPhoneNr(phoneNr);
        this.setPassword(password);
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

    public int getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr =  Integer.parseInt(phoneNr);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public final void setCalendar(DB db, ModelCache modelCache){
         calendar = new Calendar(this.getId(), db, modelCache, "UserID");

    }

    public static ObservableList<User> searchForUser(String UserName, DB db, ModelCache mc) throws SQLException, DBConnectionException {

    	ObservableList<User> searchResults = FXCollections.observableArrayList();
    	ResultSet rs;
        rs = db.query("SELECT UserID FROM USER WHERE FirstName like '%" + UserName + "%' OR LastName like '%" + UserName + "%' OR Email like '%" + UserName + "%'");

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

    public void insertToDB(DB db, User user){
        try{String sql = "INSERT INTO USER (Email, LastName, FirstName, PhoneNr, Password)\n" +
                "VALUES ('" +
                user.getEmail()+ "',\n'" +
                user.getLastName()+ "',\n'" +
                user.firstName+ "',\n'" +
                user.getPhoneNr() + "',\n'" +
                user.password + "')";
                db.insert(sql);
        }catch(SQLException e){e.printStackTrace();
        }catch (DBConnectionException e){e.printStackTrace();}
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
