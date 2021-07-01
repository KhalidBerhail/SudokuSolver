package application;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import cmd.Move;
import helpers.CheckerFactory;
import helpers.Difficulty;
import helpers.GridHelper;
import heuristics.Burma;
import heuristics.Coloring;
import heuristics.IdenticalCandidates;
import heuristics.InteractionsBetweenRegion;
import heuristics.IsolatedGroups;
import heuristics.Jellyfish;
import heuristics.MixedGroups;
import heuristics.OneCandidate;
import heuristics.Squirmbag;
import heuristics.Swordfish;
import heuristics.TwinsAndTriplet;
import heuristics.UniqueCandidate;
import heuristics.Xwing;
import heuristics.XyWing;
import model.Cell;
import model.Coordinate;
import model.Grid;
import model.History;
import model.StdCell;
import model.StdHistory;
import components.CandidatGridPane;
import components.CandidatPane;
import components.CandidatTextField;
import components.CustomPane;
import components.DefaultGridPane;
import components.DefaultPane;
import components.DefaultTextField;
import components.SelectHighlighter;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

public class GamePageController {
	
	//Panes
	@FXML
	private Pane mainPane, settingsPane, blurPane, settingStack;
	
	//TextFlow
	@FXML
    private TextFlow dialogFlow;
	
	//Buttons
    @FXML
    private Button edit_button, erase_button,  help_button, homeButton, settingsButton, redo_arrow,
    undo_arrow, solve_button, checkProgress_Button, resetButton, newGameButton, return_button,
    return2, difficulty_button, closeSettings, saveFileButton, startButton, import_button;
    
    //RadioButtons
    @FXML
    private RadioButton easyRadio, meduimRadio, hardRadio;
    
    //VBoxes
    @FXML
    private VBox newGame_VBOX;
    
    //HBoxes
    @FXML
    private HBox settings_HBOX, difficulty_VBOX;
	
    //SudokuGridModel
	private Grid grid;
	
	//CustomGridPane that hosts the sudokuGrid
	private DefaultGridPane sudokuGrid;
	
	//Array of Panes the different Panes that hold the components of each Cell
	private Pane[][] cellPanes;
	
	//List of the TextFields that represent the cells with value!=null
	private List<DefaultTextField> cells;
	
	//List of grids that represent the cells with no value
	private List<CandidatGridPane> candidateGrids;
	
	//History 
	private StdHistory<Move> history;
	
	//Group of radio buttons for difficulty level selection
	private ToggleGroup toggleGroup;
	
	//File chooser to import or save a grid
	private FileChooser fileChooser = new FileChooser();
	
	//List of cells that were checked
	private List<CustomPane> checkedCells,reasonCells;
	
	//Hidden state of settings menu
	private boolean ishiden = false;
	
	private SelectHighlighter hilighter;
	
	//Checker to verify if the value of a cell is right or wrong
	private CheckerFactory checker  ;
	
	//number of cells per line and column
	private final double CELL_NUMBER = 9;
	
	//grid width
	private final double GRID_WIDTH = 490;
	//grid height
	private final double GRID_HEIGHT = 490;
	//cell width
	private final double CELL_WIDTH = GRID_WIDTH/CELL_NUMBER;
	//cell height
	private final double CELL_HEIGHT = GRID_HEIGHT/CELL_NUMBER;
	//candidate text field width
	private final double CANDIDAT_WIDTH = 20;
	//candidate text field height
	private final double CANDIDAT_HEIGHT = 20;
	//history capacity
	private final int HISTORY_CAPACITY = 100;
	
	
	
	
	//Constructor
	public GamePageController() {
		
	}
	
	//Method that initializes the app
	@FXML
	public void initialize() {
		
		//initializing the different collections of the app
		cells = new LinkedList<DefaultTextField>();
		candidateGrids = new LinkedList<CandidatGridPane>();
		cellPanes = new Pane[(int)CELL_NUMBER][(int)CELL_NUMBER];
		checkedCells = new LinkedList<CustomPane>();
		reasonCells = new LinkedList<CustomPane>();
		hilighter = new SelectHighlighter();
		
		//Creating the toggle group
		toggleGroup = new ToggleGroup();
		
		//adding the difficulty radio buttons to the toggle group
		easyRadio.setToggleGroup(toggleGroup);
		meduimRadio.setToggleGroup(toggleGroup);
		hardRadio.setToggleGroup(toggleGroup);
		
		//setting the fileChooser title
		fileChooser.setTitle("Open Resource File");
	}
	
	public void createGraphicGrid() {
		
		//initializing the graphic grid
		sudokuGrid = new DefaultGridPane();
		sudokuGrid.styleGridPane(GRID_WIDTH,GRID_HEIGHT);
		mainPane.getChildren().add(sudokuGrid);
		
		
		CandidatGridPane candidatGrid;
		DefaultTextField DTF;
		DefaultPane cellPane;
		CandidatPane candidatPane;
		
		/*
		 * iterating the grid and  placing components for each cell based on cell nature 
		 * (CustomPane that contains a DefaultTextField for cells that have a value
		 * and a CustomPane that contains a CandidatGridPane for cells with no value)
		 * */
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				
				//checking if the cell at (x,y) has a value
				if(grid.getCellAt(x, y).getValue()!=null) {
					
					  //Creating DefaultTextField that will display the value and setting it's position
					  DTF = new DefaultTextField(CELL_WIDTH, CELL_HEIGHT);
					  DTF.setPositionX(x);
					  DTF.setPositionY(y);
					  
					  //Setting the Appendable of DTF and adding it to the cells list
					  DTF.setAppendable(grid.getCellAt(x, y).getValue());
					  cells.add(DTF);
					  
					  //Creating DefaulTPane that will contain the TextField and setting it's position
					  cellPane = new DefaultPane();
					  cellPane.setPositionX(x);
					  cellPane.setPositionY(y);
					  cellPane.definePaneParams(CELL_HEIGHT, CELL_WIDTH);
					  
					  //adding the DefaultPane to the cellPanes Array
					  cellPanes[x][y] = cellPane;
					  
					  //adding the text field to the defaultPane and styling the pane
					  cellPane.getChildren().add(DTF);
					  cellPane.stylePane(x,y);
					  
					  //adding the pane to the graphic grid
					  sudokuGrid.add(cellPane, y,x);
					  
				} else {
					
					//Creating the candidates pane that will hold the candidates Grid
					candidatPane = new CandidatPane();
					cellPanes[x][y]=candidatPane;
					candidatPane.definePaneParams(CELL_HEIGHT, CELL_WIDTH);
					candidatPane.setPositionX(x);
				 	candidatPane.setPositionY(y);
				 	sudokuGrid.add(candidatPane, y, x);
					
					//Creating the Candidates Grid and styling it
					candidatGrid = new CandidatGridPane();
					candidatGrid.styleGridPane(CELL_WIDTH, CELL_HEIGHT);
					candidatGrid.setPositionX(x);
				 	candidatGrid.setPositionY(y);
				 	candidateGrids.add(candidatGrid);
					
					
					//adding the candidates grid to the pane and styling the pane
					candidatPane.getChildren().add(candidatGrid);
					candidatPane.stylePane(x, y);
					
					
					//adding the eventHandlers for cellPanes
				 	candidatGrid.addEventHandlers(cellPanes);
				 	
				 	
				 	
				 	
				}
			}
		}
		
		//Filling the cells that have no values
		fillGraphicGrid();
		
		//Adding DefaultTextField listeners 
		for(DefaultTextField f: cells) {
			addDefaultFieldListeners(f);
		 }
	}
	
	//Method that fills the cells that have no values with candidates
	private void fillGraphicGrid() {
		CandidatTextField CTF;
		
		//Appending the value for each DefaultTextField
		for(DefaultTextField area: cells) {
			area.appendText(area.getAppendable());
		}
		for(CandidatGridPane tempGrid: candidateGrids) {
			  
			  int[][]sample= {{1,2,3},{4,5,6},{7,8,9}};
			  
			  //for each candidate grid adding the candidate text fields and appending the candidates
			  for(int i=0;i<3;i++) {
			 		 for(int j=0;j<3;j++) {
			 			 
			 			//Creating a new candidate text fields
			 			CTF = new CandidatTextField(CANDIDAT_WIDTH,CANDIDAT_HEIGHT);
			 			CTF.setBackground(Background.EMPTY);
			 			CTF.setEditable(false);
			 			
			 			//Adding the listeners for the candidate text field
			 			addCandidatFieldListeners(CTF);
			 			
			 			//adding the text field to the candidate grid
			 			tempGrid.add(CTF, j, i);
			 			
			 			//appending the candidate in the text field
			 			if(grid.getCellAt(tempGrid.getPositionX(),tempGrid.getPositionY()).getCandidates().contains(""+sample[i][j])) {
			 				CTF.appendText(""+sample[i][j]);
			 				CTF.setFont(Font.font("Verdana", FontWeight.BOLD, 8));
			 				
			 			}
						  
						  
				 	  }
		     }
		 }
	}

	//Method that adds Handlers to a candidate text field
	private void addCandidatFieldListeners(CandidatTextField ctf) {
		
		//attributing cellPanes to the textField's Highlighter
		ctf.getHighlighter().attributePanes(cellPanes);
		
		//defining cursor behavior on mouse enter
		ctf.setOnMouseEntered(new EventHandler<MouseEvent>() {
			   @Override
				public void handle(MouseEvent arg0) {
				   ctf.setCursor(Cursor.DEFAULT);
				}
		});
		
		//defining what should happen if a candidate is clicked
		ctf.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				
				CustomPane pane;
				boolean selected;
				pane = (CustomPane)ctf.getParent().getParent();
				selected = pane.getModel().isSelected();
				
				
				//checking if the cell is selected
				if(selected) {
					
					//checking if candidate text field has any text
					if(!ctf.getText().equals("")) {
						
						//Creating the new Text Field to display the clicked value
						DefaultTextField candidat = new DefaultTextField(CELL_WIDTH,CELL_HEIGHT);	
						candidat.setCanEdit(true);
						addDefaultFieldListeners(candidat);
						candidat.setPositionX(pane.getPositionX());
						candidat.setPositionY(pane.getPositionY());
						
						//Appending the clicked value to the added text field
						candidat.appendText(ctf.getText());
						
						//creating a new move
						Move mv = new Move();
						
						//defining the action of the move
						mv.addAction(Move.SET_VALUE,grid.getCellAt(pane.getPositionX(), pane.getPositionY()), ctf.getText());
						mv.act();
						//adding the move to the history
						history.add(mv);
						
						candidat.getModel().setCanEdit(true);
						pane.getChildren().clear();
						pane.getChildren().add(candidat);
						pane.getChildren().get(0).setStyle("-fx-text-fill: #fa8132;");
						pane.getModel().setSelected(true);
					}
				}
				else {
					   //highlighting the clicked cell
						ctf.getHighlighter().undoCellSelections();
						ctf.getHighlighter().highLightSelection(pane.getPositionX(),pane.getPositionY());
						pane.getModel().setSelected(true);
					
				}
				
				ctf.getHighlighter().undoSelectionHighLight(pane.getPositionX(),pane.getPositionY());
				ctf.getHighlighter().highLightSelection(pane.getPositionX(),pane.getPositionY());
			}
			
		});
		
	}

	//adding handlers to a Default Text field
	private void addDefaultFieldListeners(DefaultTextField dtf) {
		
		dtf.getHighlighter().attributePanes(cellPanes);
		
		//defining cursor behavior and actions on mouse enter 
		dtf.setOnMouseEntered(new EventHandler<MouseEvent>() {
	   		@Override
	   		public void handle(MouseEvent arg0) {
	   			dtf.setCursor(Cursor.DEFAULT);
	   			dtf.getHighlighter().highLightCellHovered((Pane)dtf.getParent());
	   		}	   	
	   	});
		
		//defining cursor behavior and actions on mouse Leave 
		dtf.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {

				 dtf.getHighlighter().undoCellHighLight((CustomPane)dtf.getParent());
				 
			}
		});
		
		//Highlighting cell on mouse click
		dtf.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				//getting the pane that contains the clicked text field
				CustomPane pane = (CustomPane)dtf.getParent();
				
				//selection state of the pane
				boolean selected = pane.getModel().isSelected();
				
				if(!selected) {
					//undoing the highlight of the previous selected cell
					dtf.getHighlighter().undoCellSelections();
					
					//undoing the previous highlighted cells
					dtf.getHighlighter().undoSelectionHighLight(pane.getPositionX(), pane.getPositionY());
					
					//highlighting the clicked cell
					dtf.getHighlighter().highLightSelection(pane.getPositionX(),pane.getPositionY());
					pane.getModel().setSelected(true);
				} 
			}
			
		});
	}
	
	//add propertyListeners 
	private void addGridChangeListeners() {
		
		//Defining the behavior if a candidate is eliminated 
		grid.addPropertyChangeListener("CANDIDAT_ELIMINATED", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				
				// getting the PropertyChange cell and extracting the data
				StdCell.PropertyChangeCell value = ((StdCell.PropertyChangeCell)arg0.getNewValue());
				String candidat = value.getCandidate();
				int X = value.getCell().getCoordinate().getX();
				int Y = value.getCell().getCoordinate().getY();
				
				//checking if the pane at (X,Y) does not contain a DefaultTextField
				if(!(cellPanes[X][Y].getChildren().get(0) instanceof DefaultTextField)) {
					
					//getting the candidate grid
					CandidatGridPane cgp = (CandidatGridPane)cellPanes[X][Y].getChildren().get(0);
					CandidatTextField ctf;
					
					//finding the CandidatTextField that contains the candidate to eliminate and clearing it
					for(Node n: cgp.getChildren()) {
						ctf = (CandidatTextField) n;
						if(ctf.getText().equals(candidat)) {
							ctf.clear();
							ctf.appendText("");
						}
					}
				}
				
				
				
			}
			
		});
		
		//Defining the behavior if the value of a cell is set
		grid.addPropertyChangeListener("VALUE_DEFINED", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				
				// getting the PropertyChageCell and extracting the data
				StdCell.PropertyChangeCell changeCell = ((StdCell.PropertyChangeCell)arg0.getNewValue());
				String value = changeCell.getValue();
				int X = changeCell.getCell().getCoordinate().getX();
				int Y = changeCell.getCell().getCoordinate().getY();
				
				//removing the extracted value from the entire line
				for(int i = 0; i < 9; i++) {
					grid.getCellAt(X, i).eliminateCandidate(value);
				}
				
				//removing the extracted value from the entire column
                for(int i = 0; i < 9; i++) {
                	grid.getCellAt(i, Y).eliminateCandidate(value);
				}
                
              //removing the extracted value from the entire area
                StdCell cell;
                for(Cell c: grid.getRegion(X, Y)) {
                	cell = (StdCell) c;
                	cell.eliminateCandidate(value);
                }
                candidatesToValue((CustomPane)cellPanes[X][Y],value);
                
                
			}
			
		});
		
		//Defining the behavior if the value of a cell is set to null
		grid.addPropertyChangeListener("VALUE_REMOVED", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				
				// getting the PropertyChageCell and extracting the data
				StdCell.PropertyChangeCell changeCell = ((StdCell.PropertyChangeCell)arg0.getOldValue());
				String value = changeCell.getValue();  
				int X = changeCell.getCell().getCoordinate().getX();
				int Y = changeCell.getCell().getCoordinate().getY();
				
				//adding the extracted value to the entire line
				for(int i = 0; i < 9; i++) {
					grid.getCellAt(X, i).addCandidate(value);
				}
				
				//adding the extracted value to the entire column
                for(int i = 0; i < 9; i++) {
                	grid.getCellAt(i, Y).addCandidate(value);
				}
                
              //adding the extracted value to the entire area
                StdCell cell;
                for(Cell c: grid.getRegion(X, Y)) {
                	cell = (StdCell) c;
                	cell.addCandidate(value);
                }
                grid.getCellAt(X,Y).unLock();
			}
			
		});
		
		//Defining the behavior if a candidate is added
		grid.addPropertyChangeListener("CANDIDAT_ADDED", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				
				// getting the PropertyChageCell and extracting the data
				StdCell.PropertyChangeCell changeCell = ((StdCell.PropertyChangeCell)arg0.getNewValue());
				String value = changeCell.getCandidate();  
				int X = changeCell.getCell().getCoordinate().getX();
				int Y = changeCell.getCell().getCoordinate().getY();
				CandidatGridPane existing;
				
				
				for(int i=0; i<9;i++) {
					
					//setting candidates of the entire line
					grid.getCellAt(X, i).setCandidates(grid.getPossibleCandidat(X, i));
					//setting candidates of the entire column
					grid.getCellAt(i,Y).setCandidates(grid.getPossibleCandidat(i,Y));
					
					
					if(i!=X || i!=Y) {
						
						//Checking if the pane at (X,Y) is an instance of CandidatPane
						if((cellPanes[X][i] instanceof CandidatPane)) {
							
							//appending the candidate in the line
							if(cellPanes[X][i].getChildren().get(0) instanceof CandidatGridPane) {
								
								existing = (CandidatGridPane) cellPanes[X][i].getChildren().get(0);
								existing.appendCandidat(value);
							}
							
						}
						
						if((cellPanes[i][Y] instanceof CandidatPane)) {
							if(cellPanes[i][Y].getChildren().get(0) instanceof CandidatGridPane) {
								//appending the candidate in the column
								existing = (CandidatGridPane) cellPanes[i][Y].getChildren().get(0);
								existing.appendCandidat(value);
							}
							
						}
					}
					
				}
				
				
			}
			
		});
		
		//Defining the behavior if goForwrd of history was called
		history.addPropertyChangeListener(History.PROP_REDO, new PropertyChangeListener() {

           @Override 
           public void propertyChange(PropertyChangeEvent arg0){
        	   // TODO Auto-generated method stub
        	   if((boolean)arg0.getNewValue()) {
        		   if(redo_arrow.isDisabled()) {
        			   redo_arrow.setDisable(false);
        		   }
        		  
        	   } else {
        		   if(!redo_arrow.isDisabled()) {
        			   redo_arrow.setDisable(true);
        		   }
        	   }
           
           }
			
		});
		
		history.addPropertyChangeListener(History.PROP_UNDO, new PropertyChangeListener() {

	           @Override 
	           public void propertyChange(PropertyChangeEvent arg0){
	        	   // TODO Auto-generated method stub
	        	   if(!(boolean)arg0.getNewValue()) {
	        		   if(!undo_arrow.isDisabled()) {
	        			   undo_arrow.setDisable(true);
	        		   }
	        		  
	        	   } else {
	        		   if(undo_arrow.isDisabled()) {
	        			   undo_arrow.setDisable(false);
	        		   }
	        	   }
	           
	           }
				
			});
		
		
		//Behavior when solve button is clicked
		solve_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				    Move solution = null;
				    boolean stop=false;
				    history.clearAll();
					int count = 0;
					while(!stop) {
						
						 solution=new OneCandidate(grid).getSolution();
						 if(solution==null || solution.getActions().size() == 0) {
							 solution=new UniqueCandidate(grid).getSolution();
						 }
						 
									
						if(solution==null || solution.getActions().size() == 0) {
							 solution=new IdenticalCandidates(grid).getSolution();	 
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution=new TwinsAndTriplet(grid).getSolution();
							 
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution=new InteractionsBetweenRegion(grid).getSolution();	 
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution=new IsolatedGroups(grid).getSolution();	 
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution = new MixedGroups(grid).getSolution();
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution = new Xwing(grid).getSolution();
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution = new XyWing(grid).getSolution();
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution = new Coloring(grid).getSolution();
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution = new Burma(grid).getSolution();
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution = new Squirmbag(grid).getSolution();
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution = new Swordfish(grid).getSolution();
						 }
						 if(solution==null || solution.getActions().size() == 0) {
							 solution = new Jellyfish(grid).getSolution();
						 }
						 if(solution!=null && solution.getActions().size() != 0) {
							 
							 solution.act();
							 
						 }
							 
						
						 if(solution==null) {
							 stop=true;
						 } else {
							 count++;
						 }
						 
						 if(solution!=null) {
							
							 if(count==1) {
								 dialogFlow.getChildren().clear();
								 Text title = new Text("FullySolved\n");
								 title.setFill(Color.web("#fa8132"));
								 title.getStyleClass().add("fullySolved");
								 dialogFlow.getChildren().add(title);
							 }
							 String s = solution.getDetails();
							 String[] array = s.split(":");
							 
							
							
							Text heuristique = new Text(array[0]+":");
							heuristique.setFill(Color.web("#fa8132"));
							heuristique.getStyleClass().add("heuristiqueName");
							Text hint = new Text(array[1]+"\n");
							hint.setFill(Color.BLACK);
							hint.getStyleClass().add("heuristiqueMessage");
							
							
							dialogFlow.getChildren().add(heuristique);
							dialogFlow.getChildren().add(hint);
						 }
						 
						
						 
					}
					 GridHelper.Save(grid.getGrid());
				
				   
				    
				   
				    
				  
				    
				   
				    }
			
			
		});	
		
		
	}
	
	//method that binds the action handlers of the different app buttons
	private void optionListeners() {
		
		//Behavior when hint button is clicked
		help_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				hilighter.undoReasonHighlight(reasonCells);
				//Node that will contain the defaultTextField of the cell that's being checked
				Node paneChild;
				//Position of the defaultTextField
				int panePositionX;
				int panePositionY;
				
				//DefaultTextField that represents the cell with a wrong value
				DefaultTextField wrong;
				
				//boolean variable that will be used to check if there are any cells with wrong values
				boolean iswrong = false;
				
				//variable that indicates the number of wrong cells found
				int count = 0;
				
				//Checking if there is any cells with incorrect values 
				
				//iterating the array of panes "cellPanes"
				for(Pane[] array : cellPanes) {
					for(Pane cellPane: array) {
						
						//getting the child node
						paneChild = cellPane.getChildren().get(0); 
						
						//checking if the child node is a DefaultTextField
						if(paneChild instanceof DefaultTextField) {
							
							//getting it's coordinates
							panePositionX = ((CustomPane) cellPane).getPositionX();
							panePositionY = ((CustomPane) cellPane).getPositionY();
							
							//checking if the cell at (panePositionX, panePositionY) has a wrong value
							if(!checker.Check(panePositionX, panePositionY)) {
								
								count++;
								//when the first wrong value is detected we show a specific message
								if(count==1) {
									
									//Clearing the text flow that we use to prompt the hints
									dialogFlow.getChildren().clear();
									
									//creating a new text that will serve as a title for our hint
									Text title = new Text("Hint!!");
									
									/*adding a class name to it's style class to give
									it a look that indicates something is wrong*/
									title.getStyleClass().add("wrongValueTitle");
									title.setFill(Color.web("#fa8132"));
									
									//adding the title to the dialog flow
									dialogFlow.getChildren().add(title);
								}
								
								//getting the defaultTextField that represents the cell with the wrong value 
								wrong = (DefaultTextField)cellPane.getChildren().get(0);
								
								//highlighting the pane that contains it
								wrong.getHighlighter().highlightWrongCell(cellPane);
								
								//marking it as checked
								((CustomPane) cellPane).setChecked(true);
								checkedCells.add((CustomPane)cellPane);
								
								//creating a new text that indicates the presence of a wrong value
								Text message = new Text("\nmauvaise valeur: " + wrong.getText() 
								                        +" AT: row:" + wrong.getPositionX()
						                                +", column:" + wrong.getPositionY());
								message.getStyleClass().add("wrongValueMessage");
								message.setFill(Color.web("rgba(255, 0, 13,1)"));
								
								//adding the message to the dialogFlow
								dialogFlow.getChildren().add(message);
								//setting iswrong to true to signal the presence of a wrong value
								iswrong = true;
							} 
						}
					}
				}
				
				
				//checking if any wrong values where found
				if(!iswrong) {
					
					//Creating the move that will represent a possible solution
					Move solution = null;
					
					//trying the OneCandidate algorithm on the grid
	                solution=new OneCandidate(grid).getSolution();
					
					 //if no solution was found with UniqueCandidate or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						 //trying the UniqueCandidate on the grid
						 solution=new UniqueCandidate(grid).getSolution();
					 }
					 
					 //if no solution was found with IdenticalCandidates or there are no actions to perform			
					if(solution==null || solution.getActions().size() == 0) {
						//trying the IdenticalCandidates on the grid
						 solution=new IdenticalCandidates(grid).getSolution();	 
					 }
					
					//if no solution was found with TwinsAndTriplet or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the TwinsAndTriplet on the grid
						 solution=new TwinsAndTriplet(grid).getSolution();
						 
					 }
					 
					//if no solution was found with InteractionsBetweenRegion or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the InteractionsBetweenRegion on the grid
						 solution=new InteractionsBetweenRegion(grid).getSolution();	 
					 }
					 
					//if no solution was found with IsolatedGroups or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the IsolatedGroups on the grid
						 solution=new IsolatedGroups(grid).getSolution();	 
					 }
					 
					//if no solution was found with MixedGroups or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the MixedGroups on the grid
						 solution = new MixedGroups(grid).getSolution();
					 }
					 
					//if no solution was found with Xwing or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the Xwing on the grid
						 solution = new Xwing(grid).getSolution();
					 }
					 
					//if no solution was found with XyWing or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the XyWing on the grid
						 solution = new XyWing(grid).getSolution();
					 }
					 
					//if no solution was found with Coloring or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the Coloring on the grid
						 solution = new Coloring(grid).getSolution();
					 }
					 
					//if no solution was found with Burma or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the Burma on the grid
						 solution = new Burma(grid).getSolution();
					 }
					 
					//if no solution was found with Squirmbag or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the Squirmbag on the grid
						 solution = new Squirmbag(grid).getSolution();
					 }
					 
					//if no solution was found with Swordfish or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the Swordfish on the grid
						 solution = new Swordfish(grid).getSolution();
					 }
					 
					//if no solution was found with Jellyfish or there are no actions to perform
					 if(solution==null || solution.getActions().size() == 0) {
						//trying the Jellyfish on the grid
						 solution = new Jellyfish(grid).getSolution();
					 }
					 
					 //if a solution was found or there is at least one action to execute
					 if(solution!=null && solution.getActions().size() != 0) {
						 
						 //adding the solution to the history
						 history.add(solution);
						 
						 //executing the action
						 solution.act() ;
						 
						 //clearing the dialog flow 
						 dialogFlow.getChildren().clear();
						 
						 //creating title for the hint and styling it
						 Text title = new Text("Hint\n");
						 title.setFill(Color.web("#fa8132"));
						 title.getStyleClass().add("fullySolved");
						 
						 //adding the title
						 dialogFlow.getChildren().add(title);
							
						 //getting the details that need to be added to the dialog flow
						 String s = solution.getDetails();
						 String[] array = s.split(":");
							 
							
						 //creating a new text that contains the name of the used algorithm and styling it	
						 Text heuristique = new Text(array[0]+":");
						 heuristique.setFill(Color.BLUE);
						 heuristique.getStyleClass().add("heuristiqueName");
						 
						 //creating a new text that contains the description of the hint
						 Text hint = new Text(array[1]+"\n");
						 hint.setFill(Color.BLACK);
						 hint.getStyleClass().add("heuristiqueMessage");
						
						 //adding the algorithm title to the  text flow
						 dialogFlow.getChildren().add(heuristique);
						 //adding the hint description to the text flow
						 dialogFlow.getChildren().add(hint);
						 
						 /***in the case of a hint that removes candidates only****/
						 CustomPane p;
						 //for each cell that was affected by an algorithm that eliminates candidates
						 for(Cell c: solution.getReasons()) {
							 
							//getting the pane that represents the cell
							p = (CustomPane)cellPanes[c.getCoordinate().getX()][c.getCoordinate().getY()];
							reasonCells.add(p);
							//highlighting the pane
							p.getHighlighter().highlightReason(p);
					     }
					 
					 
						 
					 }
				}
				
				 
				 
			}
			
		});
		
	    //Behavior when edit button is clicked
	    edit_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				//Removing reason highlight
				hilighter.undoReasonHighlight(reasonCells);
				
				//emptying dialog flow
				dialogFlow.getChildren().clear();
				
				//pane the will contain the object of the selected pain
				CustomPane selectedPane = null;
				
				//getting the selected pane
				for(Pane[] pane_Array: cellPanes) {
					for(Pane p : pane_Array) {
						if(p instanceof CustomPane) {
							if(((CustomPane) p).getModel().isSelected()) {
								selectedPane = (CustomPane)p;
								break;
							}
						}
					}
				}
				
				//Switching from a value display to a candidate display
				valueTocandidates(selectedPane,true);
				
			}
	    	
	    });
	    
	    //Behavior if progress button is clicked 
	    checkProgress_Button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				//Removing reason highlight
				hilighter.undoReasonHighlight(reasonCells);
				//Node that will contain the default text field that is being checked
				Node paneChild;
				
				//the pane's position
				int panePositionX;
				int panePositionY;
				
				//the defaultTextField that represents the cell being verified
				DefaultTextField cell;
				
				//variable used to check the number of cells checked
				int count = 0;
				
				//iterating the array of panes
				for(Pane[] array : cellPanes) {
					for(Pane cellPane: array) {
						
						//getting the child node of the current pane
						paneChild = cellPane.getChildren().get(0); 
						
						//checking if the node is an editable DefaultTextField 
						if(paneChild instanceof DefaultTextField && ((DefaultTextField) paneChild).canEdit()) {
							
							//getting the position of the pane
							panePositionX = ((CustomPane) cellPane).getPositionX();
							panePositionY = ((CustomPane) cellPane).getPositionY();
							
							//checking if the value of the cell at (panePositionX,panePositoinY)
							if(!checker.Check(panePositionX, panePositionY)) {
								
								//incrementing the number of cells checked
								count++;
								
								//checking if this is the first checked cell
								if(count==1) {
									
									//Clearing the text flow
									dialogFlow.getChildren().clear();
									
									//creating a title and styling it
									Text title = new Text("Progress:");
									title.getStyleClass().add("ProgressTitle");
									title.setFill(Color.web("#fa8132"));
									
									//adding the title to the text flow
									dialogFlow.getChildren().add(title);
								}
								
								//Getting the text field of the current cell
								cell = (DefaultTextField)cellPane.getChildren().get(0);
								
								//highlighting the pane as a wrong cell
								cell.getHighlighter().highlightWrongCell(cellPane);
								
								//creating the text to indicate that a value is wrong
								Text message = new Text("\nmauvaise valeur: " + cell.getText() 
								                        +" AT: row:" + cell.getPositionX()
						                                +", column:" + cell.getPositionY());
								
								//styling the message
								message.getStyleClass().add("wrongValueMessage");
								message.setFill(Color.web("rgba(255, 0, 13,1)"));
								
								//adding the message to the text flow
								dialogFlow.getChildren().add(message);
								
							} else {
								count++;
								//checking if this is the first checked cell
								if(count==1) {
									
									//Clearing the text flow
									dialogFlow.getChildren().clear();
									
									//creating a title and styling it
									Text title = new Text("Progress:");
									title.getStyleClass().add("ProgressTitle");
									title.setFill(Color.web("#fa8132"));
									
									//adding the title to the text flow
									dialogFlow.getChildren().add(title);
								}
								
								//Getting the text field of the current cell
								cell = (DefaultTextField)cellPane.getChildren().get(0);
								
								//highlighting the pane as a wrong cell
								cell.getHighlighter().highlightRightCell(cellPane);
								
								//creating the text to indicate that a value is right
								Text message = new Text("\nBonne valeur: " + cell.getText() 
								                        +" AT: row:" + cell.getPositionX()
						                                +", column:" + cell.getPositionY());
								message.getStyleClass().add("wrongValueMessage");
								
								//adding the message to the text field
								dialogFlow.getChildren().add(message);
								
							}
							
							//setting the checked property of the pane to true 
							((CustomPane) cellPane).setChecked(true);
							
							//adding the cell to the list of checked cells
							checkedCells.add((CustomPane)cellPane);
							
						}
					}
				}
				
			}
	    	
	    });
	    //clique sur reset
	    resetButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				//Undoing reason highlight
				hilighter.undoReasonHighlight(reasonCells);
				
				//Clearing the dialog flow
				dialogFlow.getChildren().clear();
				
				//creating a new alert to request reset confirmation
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Reset Confirmation");
				alert.setHeaderText("By reseting the grid you will loose all the progress you made ");
				alert.setContentText("Are you sure you want to reset the puzzle?");
                ButtonType confirm = new ButtonType("Confirm",ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("Cancel",ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(confirm,cancel);
                
                //showing the alert
				Optional<ButtonType> result = alert.showAndWait();
				//if confirm is clicked
				if (result.get() == confirm){
				    
					//reseting the grid
					resetGrid();
					ShowParamMenu();
					resetSettings();
					
				} else {
				    // ... user chose CANCEL or closed the dialog
				}
				
			}

			//reseting the grid
			private void resetGrid() {
				grid = GridHelper.getInitialGrid();
				addGridChangeListeners();
				history.clearAll();
				checker = new CheckerFactory(grid);
				reasonCells.clear();
				candidateGrids.clear();
				
				CustomPane parent;
				CandidatTextField CTF = null;
				CandidatGridPane CGP;
				for(int i=0; i<9; i++) {
					for(int j=0; j<9; j++) {
						if(!grid.getCellAt(i, j).isLocked()) {
							parent = (CustomPane)cellPanes[i][j];
							parent.getChildren().clear();
							CTF = new CandidatTextField(CELL_WIDTH, CELL_HEIGHT);
							CGP = new CandidatGridPane();
							CGP.styleGridPane(CELL_WIDTH, CELL_HEIGHT);
							parent.definePaneParams(CELL_HEIGHT, CELL_WIDTH);
							parent.getChildren().add(CGP);
							parent.stylePane(i, j);
							cellPanes[i][j]=parent;
							CGP.setPositionX(i);
						 	CGP.setPositionY(j);
						 	CGP.addEventHandlers(cellPanes);
						 	candidateGrids.add(CGP);
						 	parent.setPositionX(i);
						 	parent.setPositionY(j);
						 	
						 	
							
						}
					}
				}
				
				for(CandidatGridPane tempGrid: candidateGrids) {
					  
					  int[][]sample= {{1,2,3},{4,5,6},{7,8,9}};
					  for(int i=0;i<3;i++) {
					 		 for(int j=0;j<3;j++) {
					 			
					 			CTF = new CandidatTextField(CANDIDAT_WIDTH, CANDIDAT_HEIGHT);
					 			CTF.setBackground(Background.EMPTY);
					 			CTF.setEditable(false);
					 			addCandidatFieldListeners(CTF);
					 			 tempGrid.add(CTF, j, i);
					 			 if(grid.getCellAt(tempGrid.getPositionX(),tempGrid.getPositionY()).getCandidates().contains(""+sample[i][j])) {
					 				CTF.appendText(""+sample[i][j]);
					 				CTF.setFont(Font.font("Verdana", FontWeight.BOLD, 8));
					 				
					 			 }
								  
								  
						 	  }
				     }
				 }
				
				CTF.getHighlighter().undoSelectionHighLight(0,0);
				
				
				
				
				
			
			}
	    	
	    });
	        
	    //clique redo
	    redo_arrow.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				//Undoing reason highlight
				hilighter.undoReasonHighlight(reasonCells);
				
				//Clearing the dialog flow
				dialogFlow.getChildren().clear();
				
				if(!history.tailIsEmpty()) {
					history.goForward();
				Move mv = history.getCurrentElement();
				mv.act();
				
				
				if( mv.getActions().containsKey(Move.SET_VALUE)) {
					Coordinate coor = mv.getCell().getCoordinate();
					String str = mv.getActions().get(Move.SET_VALUE).getKey(mv.getCell());
					candidatesToValue((CustomPane)cellPanes[coor.getX()][coor.getY()], str);
				}
				if(mv.getActions().containsKey(Move.REMOVE_VALUE)) {
					Coordinate coor = mv.getCell().getCoordinate();
					valueTocandidates((CustomPane)cellPanes[coor.getX()][coor.getY()],false);
				}
				if(mv.getActions().containsKey(Move.ELIMINATE_CANDIDAT)) {
					for(helpers.Node<String,Cell> node: mv.getActions().get(Move.ELIMINATE_CANDIDAT).getMap()) {
					String candidat = node.getKey();
					int X = node.getValue().getCoordinate().getX();
					int Y = node.getValue().getCoordinate().getY();
					
					if(!(cellPanes[X][Y].getChildren().get(0) instanceof DefaultTextField)) {
						
						CandidatGridPane cgp = (CandidatGridPane)cellPanes[X][Y].getChildren().get(0);
						cgp.appendCandidat(candidat);
										
					}
				}
				
			
				
				}
				}
				}
	    	
	    });
	    
	  //clique undo
	    undo_arrow.setOnMouseClicked(new EventHandler<MouseEvent>() {
         
			@Override
			public void handle(MouseEvent arg0) {
				//Removing reason highlight
				hilighter.undoReasonHighlight(reasonCells);
				
				//clearing dialog flow
				dialogFlow.getChildren().clear();
				
				if(history.getCurrentPosition()!=0) {
				Move mv = history.getCurrentElement();
				mv.act();
				
			if( mv.getActions().containsKey(Move.SET_VALUE)) {
				Coordinate coor = mv.getCell().getCoordinate();
					valueTocandidates((CustomPane)cellPanes[coor.getX()][coor.getY()],false);
			}
			if(mv.getActions().containsKey(Move.REMOVE_VALUE)) {
				Coordinate coor = mv.getCell().getCoordinate();
				candidatesToValue((CustomPane)cellPanes[coor.getX()][coor.getY()], mv.getCell().getValue());
			}
			if(mv.getActions().containsKey(Move.ELIMINATE_CANDIDAT)) {
				for(helpers.Node<String,Cell> node: mv.getActions().get(Move.ELIMINATE_CANDIDAT).getMap()) {
				String candidat = node.getKey();
				int X = node.getValue().getCoordinate().getX();
				int Y = node.getValue().getCoordinate().getY();
				
				if(!(cellPanes[X][Y].getChildren().get(0) instanceof DefaultTextField)) {
					
					CandidatGridPane cgp = (CandidatGridPane)cellPanes[X][Y].getChildren().get(0);
					cgp.clearnCandidat(candidat);
									
				}
			}
				}
			
			    if(mv.getCell()!=null) {
			    	 int x = mv.getCell().getCoordinate().getX();
					    int y = mv.getCell().getCoordinate().getY();
					    
						CustomPane pane = (CustomPane)cellPanes[x][y];
						
						Node child = pane.getChildren().get(0);
						if(child instanceof DefaultTextField) {
							DefaultTextField field = (DefaultTextField) child;
							field.getHighlighter().undoCheckHighlight(pane);
						} else {
							CandidatGridPane cgd = (CandidatGridPane)child;
							CandidatTextField field = (CandidatTextField)cgd.getChildren().get(0);
							field.getHighlighter().undoCheckHighlight(pane);
						}
						
						
						
			    }
			    
			    history.goBackward();
			    
				 
				
			}
				}
	    	
	    });
	
	    //behavior on new game button click
	    newGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				hilighter.undoReasonHighlight(reasonCells);
				settings_HBOX.setVisible(!settings_HBOX.isVisible());
				return_button.setVisible(!return_button.isVisible());
				newGame_VBOX.setVisible(!newGame_VBOX.isVisible());
				closeSettings.setVisible(!closeSettings.isVisible());
				
			}
	    	
	    });
	    
	    //behavior on return button click
	    return_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				 
				return_button.setVisible(!return_button.isVisible());
				newGame_VBOX.setVisible(!newGame_VBOX.isVisible());
				settings_HBOX.setVisible(!settings_HBOX.isVisible());
				closeSettings.setVisible(!closeSettings.isVisible());
			}
	    	
	    });
	    
	    //behavior on difficulty button click
	    difficulty_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				 
				return_button.setVisible(!return_button.isVisible());
				newGame_VBOX.setVisible(!newGame_VBOX.isVisible());
				return2.setVisible(!return2.isVisible());
				difficulty_VBOX.setVisible(!difficulty_VBOX.isVisible());
				startButton.setVisible(!startButton.isVisible());
			}
	    	
	    });
	    
	    //behavior of click on return
	    return2.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) { 
				return_button.setVisible(!return_button.isVisible());
				newGame_VBOX.setVisible(!newGame_VBOX.isVisible());
				return2.setVisible(!return2.isVisible());
				difficulty_VBOX.setVisible(!difficulty_VBOX.isVisible());
				startButton.setVisible(!startButton.isVisible());
			}
	    	
	    });
	    
	    //behavior on start button clicked
	    startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// getting the selected radio button
				RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
				
				//getting the id of the selected radio button
				String toogleGroupValue = selectedRadioButton.getId();
				
				//loading the grid based on the selected difficulty
				
				switch(toogleGroupValue) {
				   case "easyRadio" : grid = GridHelper.loadGridByDif(Difficulty.EASY); break;
				   
				   case "meduimRadio" : grid = GridHelper.loadGridByDif(Difficulty.MEDUIM);break;
				   
				   case "hardRadio" : grid = GridHelper.loadGridByDif(Difficulty.HARD);break;
				   
				}
				
				//clearing all the previous elements
				sudokuGrid.getChildren().clear();
				cells.clear();
				candidateGrids.clear();
				history.clearAll();
				checker = new CheckerFactory(grid);
				createGraphicGrid();
				addGridChangeListeners();
				optionListeners();
				settingStack.toFront();
				
				ShowParamMenu();
				resetSettings();
				
			}
	    	
	    });
	    
	    //behavior on import button click
	    import_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				
				//setting the extension filer of the fileChooser
				fileChooser.getExtensionFilters().add(new ExtensionFilter("xml file","*.xml"));
				File f = fileChooser.showOpenDialog(null);
                
				//checking if a file was selected
				if(f != null) {
					
					//getting the path of the selected file
					String s = f.getPath();
					//loading the grid using the path of the selected file
					grid = GridHelper.loadGrid(s);
					
					//clearing all the collections
					sudokuGrid.getChildren().clear();
					cells.clear();
					candidateGrids.clear();
					history.clearAll();
					//Creating a new checkerFactory
					checker = new CheckerFactory(grid);
					//creating the graphic grid
					createGraphicGrid();
					//adding the grid handlers
					addGridChangeListeners();
					//adding the different action handlers
					optionListeners();
					settingStack.toFront();
					dialogFlow.getChildren().clear();
					
					ShowParamMenu();
					resetSettings();
				}
			}
	    	
	    });
	    
	    //behavior on save button clicked
	    saveFileButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				fileChooser.getExtensionFilters().add(new ExtensionFilter("xml file","*.xml"));
				File f = fileChooser.showSaveDialog(null);
				if(f != null) {
					String s = f.getPath();
					GridHelper.Save(grid.getGrid(), s);
				}
			}
	    	
	    });
	    
	    
	}
 	
	//Method binded to the onMouseClick fxml property of settings button
	@FXML
    void showSettingsMenu(MouseEvent event) {
		ShowParamMenu();
    }
	
	//method that shows/hides the settings menu
    void ShowParamMenu() {
        
		Platform.runLater(() -> {
			//Creating a transition 
			TranslateTransition transition = new TranslateTransition();
			//Setting the duration of the transition
			transition.setDuration(Duration.seconds(0.5));
			//Binding the transition to the settings menu
			transition.setNode(settingStack);
			settingStack.toFront();
			
			//checking if the settings menu is hidden 
			if(ishiden) {
				//showing the menu
				transition.setToY(settingStack.getLayoutY()-405);
			}
			else {
				//hiding the menu
				transition.setToY(660);
			}
			//playing the transition
			transition.play();
			//setting the hidden value
			ishiden = !ishiden;
		});
    }

    //method that converts from a Value display to a candidate display
    public void valueTocandidates(CustomPane selectedPane,boolean act) {
 	   //node that will contain the child of the passed pane
 	   Node selectedChild;
 	   
 	   //checking if the passed pane is not null
 	   if(selectedPane != null) {
 		   
 		   //getting the child of the pane   
 		   selectedChild = selectedPane.getChildren().get(0);
 		   
 		   //checking that the node is a DefaultTextField
 		   if(selectedChild instanceof DefaultTextField) {
 			   
 			   
 			   DefaultTextField textField = (DefaultTextField) selectedChild;
 			   
 			   //checking if the textfield can be edited
 			   if(textField.getModel().canEdit() || !act) {
 		 			
 				    //Creating the grid pane that will hold the candidat text fields
 					CandidatGridPane gridPane = new  CandidatGridPane();
 					int[][]sample= {{1,2,3},{4,5,6},{7,8,9}};
 					
 					//styling the gridpane
 					gridPane.styleGridPane(0, 0);
 					
 					// checking if the conversion requires to be added to the history
 					if(act) {
 							
 						//creating a new move	
 						Move move = new Move();
 						
 						//getting the cell that's concerned by the change
 						Cell cc =grid.getCellAt(textField.getPositionX(),textField.getPositionY());
 						
 						//adding an action to the move
 						move.addAction(Move.REMOVE_VALUE, cc, cc.getValue());
 						
 						//executing to the action
 						move.act();
 						
 						//adding the move to history
 						history.add(move);
 					}
 					
 					grid.generateAllCandidat();
 					Set<Cell> relatedCells = grid.getAllCellsRelatedTo(grid.getCellAt(textField.getPositionX(),textField.getPositionY()));
 					
 					
 					for(int j=0; j<3;j++) {
 						for(int i=0; i<3;i++) {
 							CandidatTextField CTF = new CandidatTextField(CANDIDAT_WIDTH, CANDIDAT_HEIGHT);
 							CTF.setBackground(Background.EMPTY);
 							CTF.setEditable(false);
 							addCandidatFieldListeners(CTF);
 							if(grid.getCellAt(textField.getPositionX(),textField.getPositionY()).getCandidates().contains(""+sample[j][i])) {
 								CTF.appendText(""+sample[j][i]);
 								CTF.setFont(Font.font("Verdana", FontWeight.BOLD, 8));
 							}
 							gridPane.add(CTF, i, j);
 						}
 					}
 					gridPane.setPositionX(selectedPane.getPositionX());
 					gridPane.setPositionY(selectedPane.getPositionY());
 		 			gridPane.addEventHandlers(cellPanes);
 	            	selectedPane.getChildren().clear();
 					selectedPane.getChildren().add(gridPane);
 					
 					
 					
 					
 					
 					CustomPane testing;
 					Node child;
 					CandidatGridPane cgrid;
 					Cell comp;
 					CandidatTextField ctf;
 					
 					
 				
 					for(Cell c : relatedCells) {
 						testing = (CustomPane) cellPanes[c.getCoordinate().getX()][c.getCoordinate().getY()];
 						child = testing.getChildren().get(0);
 						comp = c;
 						
 						if(child instanceof CandidatGridPane) {
 							cgrid = (CandidatGridPane) child;
 							for(int x=0;x<3;x++) {
 								for(int y=0;y<3;y++){
 									ctf = new CandidatTextField(CANDIDAT_WIDTH, CANDIDAT_HEIGHT);
 									ctf.setBackground(Background.EMPTY);
 									ctf.setEditable(false);
 									addCandidatFieldListeners(ctf);
 									if(comp.getCandidates().contains(""+sample[x][y])) {
 										ctf.appendText(""+sample[x][y]);
 										ctf.setFont(Font.font("Verdana", FontWeight.BOLD, 8));
 									}
 									cgrid.add(ctf, y, x);
 								}
 							}
 						}
                     }
 					
 					//filling the candidatGridPane with candidates
 					
 				}
 		   }
 	   }
     }

    //Method that converts from a candidate display to a value display    
    public void candidatesToValue(CustomPane selectedPane ,String value) {
	
		//getting the position of the pane passed in the argument
		int X = selectedPane.getPositionX();
		int Y = selectedPane.getPositionY();
		
		//clearing the children of the pane
		cellPanes[X][Y].getChildren().clear();
	    
		//Creating a new DefaultTextField
	    DefaultTextField DTF = new DefaultTextField(CELL_WIDTH,CELL_HEIGHT);	
		DTF.setCanEdit(true);
		cellPanes[X][Y].getChildren().add(DTF);
		
		//adding listeners to it and setting it's position 
		addDefaultFieldListeners(DTF);
		DTF.setPositionX(X);
		DTF.setPositionY(Y);
		
		//appending the passed value and styling it
		DTF.appendText(value);
		DTF.setStyle("-fx-text-fill: #fa8132;");
	 
   }
    
   //Method that launches the game based on difficulty level 
    public void LaunchBydifficulty(String difficulty) {
	   switch(difficulty) {
	   case "easyRadio" : grid = GridHelper.loadGridByDif(Difficulty.EASY); break;
	   
	   case "meduimRadio" : grid = GridHelper.loadGridByDif(Difficulty.MEDUIM);break;
	   
	   case "hardRadio" : grid = GridHelper.loadGridByDif(Difficulty.HARD);break;
	   
	}
	   
	 //initializing the grid object, checker and history
		
	 		checker =  new CheckerFactory(grid);
	 		history = new StdHistory<Move>(HISTORY_CAPACITY);
	 		//Creating the graphic grid from the grid object
			createGraphicGrid();
			
			//adding changeListeners for different grid properties
			addGridChangeListeners();
			
			//adding listeners for different expected actions
			optionListeners();
   } 
   
   //Method that launches game from path
    public void LaunchFromPath(String path) {
	       grid = GridHelper.loadGrid(path);
	       //initializing the grid object, checker and history
		
	 		checker =  new CheckerFactory(grid);
	 		history = new StdHistory<Move>(HISTORY_CAPACITY);
	 		//Creating the graphic grid from the grid object
			createGraphicGrid();
			
			//adding changeListeners for different grid properties
			addGridChangeListeners();
			
			//adding listeners for different expected actions
			optionListeners();
   }
   
   //Method that resets the graphic contents of the settings menu
    private void resetSettings() {
	    return_button.setVisible(false);
	    return2.setVisible(false);
	    difficulty_VBOX.setVisible(false);
		startButton.setVisible(false);
		newGame_VBOX.setVisible(false);
	    settings_HBOX.setVisible(true);
	    closeSettings.setVisible(true);
	    easyRadio.setSelected(true);
	    hardRadio.setSelected(false);
	    meduimRadio.setSelected(false);
   }
   
   

}