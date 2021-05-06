package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Run extends Application {
    
	public static void main(String[] args) {
        launch(args);
 }

 @Override
 public void start(Stage primaryStage) throws Exception {
    String fxmlResource = "/fxml/GamePage.fxml";
   
    Parent panel;
    panel = FXMLLoader.load(getClass().getResource(fxmlResource));
    Scene scene = new Scene(panel);
    Stage stage = new Stage();
    stage.setResizable(true);
    stage.setScene(scene);
    stage.show();
 }
 
 

    
}