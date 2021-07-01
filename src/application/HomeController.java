package application;

import java.io.File;
import java.io.IOException;

import helpers.CheckerFactory;
import helpers.GridHelper;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;

public class HomeController {
	
	//Parent Pane
	@FXML
    private Pane homePane;

	//Different home buttons
    @FXML
    private Button difficulty_button_home, import_button_home, closeApp, startButtonHome, returnHome;
    
    //Title for home screen
    @FXML
    private Label homeTitle;

    //Horizontal box that groups difficulty radio buttons
    @FXML
    private HBox difficulty_VBOX;
    
    //Vertical boxes that contain difficulty and import buttons and labels
    @FXML
    private VBox diffBox,  importBox;

    //difficulty radio buttons
    @FXML
    private RadioButton easyRadio, meduimRadio, hardRadio;
    
    
    //Group of radio buttons for difficulty level selection
  	private ToggleGroup toggleGroup;
    
  	//Id of the toggled radio
  	private String toggled;
    
  	
  //File chooser to import or save a grid
  	private FileChooser fileChooser = new FileChooser();
    
    //Constructor
    public HomeController() {
    	
    }
    
    @FXML
	public void initialize() {
    	//Making background of the parent pane transparent
    	homePane.setBackground(Background.EMPTY);
    	//adding difficulty radio buttons to a to a toggle group
    	groupRadioButtons(); 
    	//adding handlers for the different buttons of the home screen
    	addHandlers();
    }
    
    
    /* 
     * Function that adds handlers that define what should be done 
     * in case a certain button is clicked */
    public void addHandlers() {
    	
    	//Behavior on difficulty button click
    	difficulty_button_home.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				//showing the difficulty radio buttons
				hideShowElements();
			}
	    	
	    });
    	
    	//Behavior on return
    	returnHome.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				//showing elements of the home screen only
				hideShowElements();
			}
	    	
	    });
    	
    	closeApp.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				//Closing the application
				Platform.exit();
			}
	    	
	    });
    	
    	startButtonHome.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// getting the selected radio button
				RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
				//getting the id of the selected radio button
				toggled= selectedRadioButton.getId();
				
				double width = 994, height = 648;
		    	FXMLLoader Loader = new FXMLLoader();
		    	Loader.setLocation(getClass().getResource("/fxml/GamePage.fxml"));
		        try {
		        	   Loader.load();
		        	} catch (IOException e) {
                       e.printStackTrace();
                    }
		        GamePageController GPC = Loader.getController();
		        GPC.LaunchBydifficulty(toggled);
		        Parent root = Loader.getRoot();
		        
		        Stage currentStage = (Stage) startButtonHome.getScene().getWindow();
		        Stage window = new Stage();
		        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		    	window.setX((screenBounds.getWidth() - width) / 2); 
		        window.setY((screenBounds.getHeight() - height) / 2);
		    	window.setScene(new Scene(root, width, height));
		    	
		    	currentStage.hide();
		    	window.show();
		    
			}
	    	
	    });
    	
    	//behavior on import button click
    	import_button_home.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				//setting the extension filer of the fileChooser
				fileChooser.getExtensionFilters().add(new ExtensionFilter("xml file","*.xml"));
				File f = fileChooser.showOpenDialog(null);
                
				//checking if a file was selected
				if(f != null) {
					
					//getting the path of the selected file
					String s = f.getPath();
					
					double width = 994, height = 648;
			    	FXMLLoader Loader = new FXMLLoader();
			    	Loader.setLocation(getClass().getResource("/fxml/GamePage.fxml"));
			        try {
			        	   Loader.load();
			        	} catch (IOException e) {
	                       e.printStackTrace();
	                    }
			        GamePageController GPC = Loader.getController();
			        GPC.LaunchFromPath(s);
			        Parent root = Loader.getRoot();
			        
			        Stage currentStage = (Stage) startButtonHome.getScene().getWindow();
			        Stage window = new Stage();
			        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			    	window.setX((screenBounds.getWidth() - width) / 2); 
			        window.setY((screenBounds.getHeight() - height) / 2);
			    	window.setScene(new Scene(root, width, height));
			    	
			    	currentStage.hide();
			    	window.show();
					
				}
			}
	    	
	    });
    }
    
    //outils
    private void hideShowElements() {
    	importBox.setVisible(!importBox.isVisible());
		diffBox.setVisible(!diffBox.isVisible());
		startButtonHome.setVisible(!startButtonHome.isVisible());
		difficulty_VBOX.setVisible(!difficulty_VBOX.isVisible());
		returnHome.setVisible(!returnHome.isVisible());
    }
    
    private void groupRadioButtons() {
    	toggleGroup = new ToggleGroup();
    	easyRadio.setToggleGroup(toggleGroup);
		meduimRadio.setToggleGroup(toggleGroup);
		hardRadio.setToggleGroup(toggleGroup);
    }
}

