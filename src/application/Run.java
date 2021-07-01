package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Run extends Application {
    
	public static void main(String[] args) {
        launch(args);
 }

 @Override
 public void start(Stage primaryStage) throws Exception {
    String fxmlResource = "/fxml/Home.fxml";
   
    Parent panel;
    panel = FXMLLoader.load(getClass().getResource(fxmlResource));
    Scene scene = new Scene(panel);
    Stage stage = new Stage();
    stage.setResizable(false);
    stage.setScene(scene);
    stage.initStyle(StageStyle.UNDECORATED);
    scene.setFill(Color.TRANSPARENT);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.setY(240);
    stage.setX(530);
    
    stage.show();
 }
 
 

    
}