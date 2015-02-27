package application;



import controllers.LoginController;
import models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import util.Config;
import util.DB;
import util.ModelCache;

import java.io.IOException;


public class Main extends Application {

    private Stage stage;
    private Config config;
    private DB db;
    private ModelCache modelCache;
    private User user;


    @Override
    public void start(Stage primaryStage) {
        try {
            this.stage = primaryStage;
            this.stage.setTitle("Calendar");
            loadConfig();
            setupDB();
            modelCache = new ModelCache();
            login();
            primaryStage.show();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
    }

    private void loadConfig() {
        config = new Config();
        try {
            config.load("config.properties");
        } catch (IOException e) {
            System.out.println("Exception " + e);
        }
    }

    private void setupDB() {
        db = new DB();
        try {
            db.configure(config);
            db.connect();
        }catch(Exception e){
            System.out.println("Exception " + e);
        }
    }

    private void login(){
        try {
            LoginController login = (LoginController) replaceSceneContent("/views/Login.fxml");
            login.setApp(this);
        }catch(Exception e){
            System.out.println("Exception " + e);
        }
    }

    public Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane page = (AnchorPane) loader.load();
        Scene pageScene = new Scene(page);
        this.stage.setScene(pageScene);;
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    public Config getConfig() {
        return config;
    }

    public DB getDb() {
        return db;
    }

    public ModelCache getModelCache() {
        return modelCache;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static void main(String[] args) {
        launch(args);
    }

}	
