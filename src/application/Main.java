package application;



import controllers.LoginController;
import models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {

    private Stage stage;
    private User user;


    @Override
    public void start(Stage primaryStage) {
        try {
            this.stage = primaryStage;
            this.stage.setTitle("Calendar");
            login();
            primaryStage.show();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
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
        AnchorPane page = loader.load();
        Scene pageScene = new Scene(page);
        this.stage.setScene(pageScene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
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
