package models;

import exceptions.DBConnectionException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import util.DB;
import util.ModelCache;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Model{

    private int id;
    private String lastName;
    private String firstName;
    private int phoneNr;
    private String password;
    private Calendar calendar;

    private StringProperty email = new SimpleStringProperty();


    public User() {
    }
    public User(String email, String lastName, String firstName, String phoneNr, String password){
        this.setEmail(email);
        this.setLastName(lastName);
        this.setFirstName(firstName);
        this.setPhoneNr(phoneNr);
        this.setPassword(password);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
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
         calendar = new Calendar(this.getId(), db, modelCache, Calendar.OwnerType.USER);
    }

    public Image getImage (DB db, ModelCache mc) {
        ResultSet rs;
        BufferedImage image = null;
        InputStream fis = null;
        Image newImage = null;
        try {
            String query = "SELECT  `Image`"+
                    "FROM USER "+
                    "WHERE  `UserID` ="+getId()+"";
            rs = db.query(query);
            if (rs.next()) {
                fis = rs.getBinaryStream("Image");
                System.out.println("fis = " + fis);
                image = ImageIO.read(fis);
                newImage = SwingFXUtils.toFXImage(image, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return newImage;
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
        else user = new User();
        user.setId(id);
        mc.put(id, user);
        user.refreshFromDB(db, mc);
        return user;
    }

    public static ObservableList<User> getAllUsers(DB db, ModelCache mc) throws SQLException, DBConnectionException {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT  `UserID` \n" +
                "FROM  `USER`";
        ResultSet rs = db.query(query);
        while (rs.next()) {
            User user = User.getById(rs.getInt("UserID"), db, mc);
            users.add(user);
        }
        return users;
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
        String sql = "UPDATE USER SET\n" +
                "EMail = '" + getEmail() + "',\n" +
                "LastName = '" + getLastName() + "',\n" +
                "FirstName = '" + getFirstName() + "',\n" +
                "PhoneNr = '" + getPhoneNr() + "'\n" +
                "WHERE UserID = " + getId();
        db.update(sql);
    }

    @Override
    public void insertToDB(DB db) throws SQLException, DBConnectionException {
        String updateSql = "INSERT INTO USER\n" +
                "(EMail, LastName, FirstName, PhoneNr)\n" +
                "VALUES (\n" +
                "'" + getEmail() + "',\n" +
                "'" + getLastName() + "',\n" +
                "'" + getFirstName() + "',\n" +
                "'" + getPhoneNr() + "' );";
        db.update(updateSql);
        String querySql = "SELECT MAX(UserID) AS ID FROM USER";
        ResultSet results = db.query(querySql);
        if (!results.next()) throw new SQLException("This shouldn't happen. Sooo...");
        setId(results.getInt("ID"));
    }
}
