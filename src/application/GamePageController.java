package application;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
import model.StdCell;
import model.StdGrid;
import model.StdHistory;
import components.CandidatGridPane;
import components.CandidatPane;
import components.CandidatTextField;
import components.CustomGridPane;
import components.CustomPane;
import components.DefaultGridPane;
import components.DefaultPane;
import components.DefaultTextField;
import javafx.animation.TranslateTransition;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
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
	
	//Pane
	@FXML
	private Pane mainPane;
	@FXML
    private Pane settingsPane;
	@FXML
    private Pane blurPane;
	@FXML
    private Pane settingStack;
	//TextArea
	@FXML
    private TextFlow dialogFlow;
	//Buttons

    @FXML
    private Button edit_button;
    @FXML
    private Button erase_button;
    @FXML
    private Button help_button;
    @FXML
    private Button homeButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button redo_arrow;
    @FXML
    private Button undo_arrow;
    @FXML
    private Button solve_button;
    @FXML
    private Button checkProgress_Button;
    @FXML
    private Button resetButton;
    @FXML
    private Button newGameButton;
    @FXML
    private Button return_button;
    @FXML
    private Button return2;
    @FXML
    private Button difficulty_button;
    @FXML
    private Button closeSettings;
    
    @FXML
    private Button saveFileButton;
    @FXML
    private RadioButton easyRadio;

    @FXML
    private RadioButton meduimRadio;

    @FXML
    private RadioButton hardRadio;
    @FXML
    private Button startButton;
    @FXML
    private Button import_button;
    @FXML
    private VBox newGame_VBOX;
    @FXML
    private HBox settings_HBOX;
    @FXML
    private HBox difficulty_VBOX;
	
	private Grid grid;
	private DefaultGridPane sudokuGrid;
	private Pane[][] cellPanes;
	private List<DefaultTextField> cells;
	private List<CandidatGridPane> candidateGrids;
	private StdHistory<Move> history;
	private ToggleGroup toggleGroup;
	private FileChooser fileChooser = new FileChooser();
	
	private List<CustomPane> checkedCells;
	
	
	private boolean ishiden = false;
	private CheckerFactory checker  ;
	public GamePageController() {
		
	}
	
	@FXML
	public void initialize() {
		cells = new LinkedList<DefaultTextField>();
		candidateGrids = new LinkedList<CandidatGridPane>();
		cellPanes = new Pane[9][9];
		grid = GridHelper.loadGrid();
		checker =  new CheckerFactory(grid);
		history = new StdHistory<Move>(100);
		createGraphicGrid();
		addGridChangeListeners();
		optionListeners();
		CandidatGridPane c = new CandidatGridPane();
		toggleGroup = new ToggleGroup();
		easyRadio.setToggleGroup(toggleGroup);
		meduimRadio.setToggleGroup(toggleGroup);
		hardRadio.setToggleGroup(toggleGroup);
		fileChooser.setTitle("Open Resource File");
		checkedCells = new LinkedList<CustomPane>();
		
		
		/*double BLUR_AMOUNT = 100;
	    Effect frostEffect = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 3);
	    blurPane.setEffect(frostEffect);*/
	}
	
	public void createGraphicGrid() {
		sudokuGrid = new DefaultGridPane();
		//sudokuGrid.defineGridConstraints(9,9);
		sudokuGrid.styleGridPane(500,500);
		mainPane.getChildren().add(sudokuGrid);
		
		
		CandidatGridPane candidatGrid;
		DefaultTextField DTF;
		DefaultPane cellPane;
		CandidatPane candidatPane;
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				if(grid.getCellAt(x, y).getValue()!=null) {
					 DTF = new DefaultTextField(500/9, 500/9);
					  cellPane = new DefaultPane();
					  cellPane.setPositionX(x);
					  cellPane.setPositionY(y);
					  cellPane.definePaneParams(500/9, 500/9);
					  cellPanes[x][y] = cellPane;
					  DTF.setPositionX(x);
					  DTF.setPositionY(y);
					  cellPane.getChildren().add(DTF);
					  cellPane.stylePane(x,y);
					  sudokuGrid.add(cellPane, y,x);
					  DTF.setAppendable(grid.getCellAt(x, y).getValue());
					  cells.add(DTF);
				} else {
					candidatPane = new CandidatPane();
					candidatGrid = new CandidatGridPane();
					candidatGrid.styleGridPane(500/9, 500/9);
					candidatPane.definePaneParams(500/9, 500/9);
					candidatPane.getChildren().add(candidatGrid);
					candidatPane.stylePane(x, y);
					cellPanes[x][y]=candidatPane;
					candidatGrid.setPositionX(x);
				 	candidatGrid.setPositionY(y);
				 	candidatGrid.addEventHandlers(cellPanes);
				 	sudokuGrid.add(candidatPane, y, x);
				 	candidateGrids.add(candidatGrid);
				 	candidatPane.setPositionX(x);
				 	candidatPane.setPositionY(y);
				 	
				}
			}
		}
		fillGraphicGrid();
		for(DefaultTextField f: cells) {
			/*******************
			 f.addEventHandlers(cellPanes);
			********************/
			addDefaultFieldListeners(f);
		 }
	}
	
	private void fillGraphicGrid() {
		CandidatTextField CTF;
		for(DefaultTextField area: cells) {
			area.appendText(area.getAppendable());
		}
		for(CandidatGridPane tempGrid: candidateGrids) {
			  
			  int[][]sample= {{1,2,3},{4,5,6},{7,8,9}};
			  for(int i=0;i<3;i++) {
			 		 for(int j=0;j<3;j++) {
			 			
			 			CTF = new CandidatTextField(20,20);
			 			CTF.setBackground(Background.EMPTY);
			 			CTF.setEditable(false);
			 			/**********
			 			CTF.addEventHandlers(cellPanes);
			 			***************/
			 			addCandidatFieldListeners(CTF);
			 			 tempGrid.add(CTF, j, i);
			 			 if(grid.getCellAt(tempGrid.getPositionX(),tempGrid.getPositionY()).getCandidates().contains(""+sample[i][j])) {
			 				CTF.appendText(""+sample[i][j]);
			 				CTF.setFont(Font.font("Verdana", FontWeight.BOLD, 8));
			 				
			 			 }
						  
						  
				 	  }
		     }
		 }
	}

	
	
	private void addCandidatFieldListeners(CandidatTextField ctf) {
		ctf.getHighlighter().attributePanes(cellPanes);
		ctf.setOnMouseEntered(new EventHandler<MouseEvent>() {
			   @Override
				public void handle(MouseEvent arg0) {
				   ctf.setCursor(Cursor.DEFAULT);
				}
		});
		
		ctf.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				
				CustomPane pane;
				boolean selected;
				pane = (CustomPane)ctf.getParent().getParent();
				selected = pane.getModel().isSelected();
				
				
				
				if(selected) {
					if(!ctf.getText().equals("")) {
						
						DefaultTextField candidat = new DefaultTextField(500/9,500/9);	
						candidat.setCanEdit(true);
						/**********************
						candidat.addEventHandlers(cellPanes);
						*****************/
						addDefaultFieldListeners(candidat);
						candidat.setPositionX(pane.getPositionX());
						candidat.setPositionY(pane.getPositionY());
						
						candidat.appendText(ctf.getText());
						//grid.getCellAt(pane.getPositionX(),pane.getPositionY()).setValue(ctf.getText());
						
						Move mv = new Move();
						mv.addAction(Move.SET_VALUE,grid.getCellAt(pane.getPositionX(), pane.getPositionY()), ctf.getText());
						mv.act();
						history.add(mv);
						
						/*if(checker.Check(pane.getPositionX(), pane.getPositionY())) {
							System.out.println("bonne valeur");
						}else System.out.println("mauvaise valeur");*/
						
						candidat.getModel().setCanEdit(true);
						pane.getChildren().clear();
						pane.getChildren().add(candidat);
						pane.getChildren().get(0).setStyle("-fx-text-fill: #fa8132;");
						pane.getModel().setSelected(true);
					}
				}
				else {
						ctf.getHighlighter().undoCellSelections();
						ctf.getHighlighter().highLightSelection(pane.getPositionX(),pane.getPositionY());
						pane.getModel().setSelected(true);
					
				}
				
				ctf.getHighlighter().undoSelectionHighLight(pane.getPositionX(),pane.getPositionY());
				ctf.getHighlighter().highLightSelection(pane.getPositionX(),pane.getPositionY());
			}
			
		});
		
	}

	private void addDefaultFieldListeners(DefaultTextField dtf) {
		dtf.getHighlighter().attributePanes(cellPanes);
		
		dtf.setOnMouseEntered(new EventHandler<MouseEvent>() {
	   		@Override
	   		public void handle(MouseEvent arg0) {
	   			dtf.setCursor(Cursor.DEFAULT);
	   			//dtf.getHighlighter().highLightSelection(dtf.getPositionX(),dtf.getPositionY());
	   			dtf.getHighlighter().highLightCellHovered((Pane)dtf.getParent());
	   		}	   	
	   	});
		
		dtf.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {

				 dtf.getHighlighter().undoCellHighLight((CustomPane)dtf.getParent());
				 
			}
		});
		
		dtf.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				CustomPane pane = (CustomPane)dtf.getParent();
				boolean selected = pane.getModel().isSelected();
				if(!selected) {
					
					dtf.getHighlighter().undoCellSelections();
					dtf.getHighlighter().undoSelectionHighLight(pane.getPositionX(), pane.getPositionY());
					dtf.getHighlighter().highLightSelection(pane.getPositionX(),pane.getPositionY());
					pane.getModel().setSelected(true);
				} else {
                    if(dtf.getModel().canEdit()) {/*
        	 			//dtf.getModel().setCandidats(grid.getCellAt(dtf.getPositionX(),dtf.getPositionY()).getCandidates());
                    	 pane = (CustomPane)dtf.getParent();
        				CandidatGridPane gridPane = new  CandidatGridPane();
        				int[][]sample= {{1,2,3},{4,5,6},{7,8,9}};
        				gridPane.styleGridPane(0, 0);
        				CandidatGridPane existing;
        				
        				grid.getCellAt(dtf.getPositionX(),dtf.getPositionY()).unLock();
        				grid.getCellAt(dtf.getPositionX(),dtf.getPositionY()).removeValue();
        				//grid.getCellAt(dtf.getPositionX(),dtf.getPositionY()).setValue("");
        				
        				
        				for(int i=0; i<9;i++) {
        					grid.getCellAt(dtf.getPositionX(), i).setCandidates(grid.getPossibleCandidat(dtf.getPositionX(), i));
        					grid.getCellAt(i,dtf.getPositionY()).setCandidates(grid.getPossibleCandidat(i,dtf.getPositionY()));
        					if(i!=dtf.getPositionX() || i!=dtf.getPositionY()) {
        						if((cellPanes[dtf.getPositionX()][i] instanceof CandidatGridPane)) {
        							existing = (CandidatGridPane) cellPanes[dtf.getPositionX()][i].getChildren().get(0);
        							//existing.appendCandidat(dtf.getText());
        						}
        						
        						if((cellPanes[dtf.getPositionY()][i] instanceof CandidatGridPane)) {
        							existing = (CandidatGridPane) cellPanes[i][dtf.getPositionY()].getChildren().get(0);
        							//existing.appendCandidat(dtf.getText());
        						}
        					}
        				}
        				
        				for(Cell c : grid.getRegion(dtf.getPositionX(), dtf.getPositionY())) {
        					c.setCandidates(grid.getPossibleCandidat(c.getCoordinate().getX(), c.getCoordinate().getY()));
        					if((cellPanes[c.getCoordinate().getX()][c.getCoordinate().getY()] instanceof CandidatGridPane)) {
    							existing = (CandidatGridPane) cellPanes[c.getCoordinate().getX()][c.getCoordinate().getY()].getChildren().get(0);
    							//existing.appendCandidat(dtf.getText());
    						}
        				}
        			
        				for(int j=0; j<3;j++) {
        					for(int i=0; i<3;i++) {
        						//CandidatPane CP = new CandidatPane();
        						CandidatTextField CTF = new CandidatTextField(20,20);
        						CTF.setBackground(Background.EMPTY);
        						CTF.setEditable(false);
        						addCandidatFieldListeners(CTF);
        						if(grid.getCellAt(dtf.getPositionX(),dtf.getPositionY()).getCandidates().contains(""+sample[j][i])) {
        							CTF.appendText(""+sample[j][i]);
        							CTF.setFont(Font.font("Verdana", FontWeight.BOLD, 8));
        						}
        						//CP.getChildren().add(CTF);
        						gridPane.add(CTF, i, j);
        					}
        				}
        				gridPane.setPositionX(pane.getPositionX());
        				gridPane.setPositionY(pane.getPositionY());
        	 			gridPane.addEventHandlers(cellPanes);
                    	pane.getChildren().clear();
        				pane.getChildren().add(gridPane);*/
					}
				}
			}
			
		});
	}
	
	
	private void addGridChangeListeners() {
		
		grid.addPropertyChangeListener("CANDIDAT_ELIMINATED", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				
				StdCell.PropertyChangeCell value = ((StdCell.PropertyChangeCell)arg0.getNewValue());
				String candidat = value.getCandidate();
				int X = value.getCell().getCoordinate().getX();
				int Y = value.getCell().getCoordinate().getY();
				
				if(!(cellPanes[X][Y].getChildren().get(0) instanceof DefaultTextField)) {
					
					CandidatGridPane cgp = (CandidatGridPane)cellPanes[X][Y].getChildren().get(0);
					CandidatTextField ctf;
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
		
		grid.addPropertyChangeListener("VALUE_DEFINED", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				StdCell.PropertyChangeCell changeCell = ((StdCell.PropertyChangeCell)arg0.getNewValue());
				String value = changeCell.getValue();
				int X = changeCell.getCell().getCoordinate().getX();
				int Y = changeCell.getCell().getCoordinate().getY();
				
				for(int i = 0; i < 9; i++) {
					grid.getCellAt(X, i).eliminateCandidate(value);
				}
				
                for(int i = 0; i < 9; i++) {
                	grid.getCellAt(i, Y).eliminateCandidate(value);
				}
                
                StdCell cell;
                for(Cell c: grid.getRegion(X, Y)) {
                	cell = (StdCell) c;
                	cell.eliminateCandidate(value);
                }
                candidatesToValue((CustomPane)cellPanes[X][Y],value);
                
                
			}
			
		});
		
		grid.addPropertyChangeListener("VALUE_REMOVED", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				StdCell.PropertyChangeCell changeCell = ((StdCell.PropertyChangeCell)arg0.getOldValue());
				String value = changeCell.getValue();  
				int X = changeCell.getCell().getCoordinate().getX();
				int Y = changeCell.getCell().getCoordinate().getY();
				
				for(int i = 0; i < 9; i++) {
					grid.getCellAt(X, i).addCandidate(value);
				}
				
                for(int i = 0; i < 9; i++) {
                	grid.getCellAt(i, Y).addCandidate(value);
				}
                
                StdCell cell;
                for(Cell c: grid.getRegion(X, Y)) {
                	cell = (StdCell) c;
                	cell.addCandidate(value);
                }
                grid.getCellAt(X,Y).unLock();
                /******for undo solve***
                valueTocandidates((CustomPane)cellPanes[X][Y]);**/
			}
			
		});
		
		grid.addPropertyChangeListener("CANDIDAT_ADDED", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				
				StdCell.PropertyChangeCell changeCell = ((StdCell.PropertyChangeCell)arg0.getNewValue());
				String value = changeCell.getCandidate();  
				int X = changeCell.getCell().getCoordinate().getX();
				int Y = changeCell.getCell().getCoordinate().getY();
				CandidatGridPane existing;
				
				CandidatTextField CTF ;
				
			
				int[][]sample= {{1,2,3},{4,5,6},{7,8,9}};
				
				for(int i=0; i<9;i++) {
					
					grid.getCellAt(X, i).setCandidates(grid.getPossibleCandidat(X, i));
					grid.getCellAt(i,Y).setCandidates(grid.getPossibleCandidat(i,Y));
					
					if(i!=X || i!=Y) {
						if((cellPanes[X][i] instanceof CandidatPane)) {
							
							if(cellPanes[X][i].getChildren().get(0) instanceof CandidatGridPane) {
								
								existing = (CandidatGridPane) cellPanes[X][i].getChildren().get(0);
								existing.appendCandidat(value);
							}
							
						}
						
						if((cellPanes[i][Y] instanceof CandidatPane)) {
							if(cellPanes[i][Y].getChildren().get(0) instanceof CandidatGridPane) {
								
								existing = (CandidatGridPane) cellPanes[i][Y].getChildren().get(0);
								existing.appendCandidat(value);
							}
							
						}
					}
					
				}
				
				
				
				
				if((cellPanes[X][Y] instanceof CandidatPane)) {
					
					/*CandidatGridPane cdp = (CandidatGridPane)cellPanes[X][Y].getChildren().get(0);
					for(Node ctf : cdp.getChildren()) {
						CTF = (CandidatTextField )ctf;
						
						if(grid.getCellAt(X,Y).getCandidates().contains(""+sample[CTF.getPositionX()][CTF.getPositionY()])) {
							CTF.appendText("E");
						}
						
					}*/
				}
				
				
			}
			
		});
		
		
		
		
		//solve in one click
		
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
								 title.setFill(Color.GREEN);
								 title.getStyleClass().add("fullySolved");
								 dialogFlow.getChildren().add(title);
							 }
							 String s = solution.getDetails();
							 String[] array = s.split(":");
							 
							
							
							Text heuristique = new Text(array[0]+":");
							heuristique.setFill(Color.BLUE);
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
	
	
	private void optionListeners() {
		
		//Click sur Hint
		help_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
				
				//Checking if there is any cells with incorrect values 
				Node paneChild;
				int panePositionX;//customPane X positoion
				int panePositionY;//customPane Y position
				DefaultTextField wrong;//TextField with wrong value
				boolean iswrong = false;
				int count = 0;
				
				for(Pane[] array : cellPanes) {
					for(Pane cellPane: array) {
						paneChild = cellPane.getChildren().get(0); 
						if(paneChild instanceof DefaultTextField) {
							panePositionX = ((CustomPane) cellPane).getPositionX();
							panePositionY = ((CustomPane) cellPane).getPositionY();
							if(!checker.Check(panePositionX, panePositionY)) {
								count++;
								if(count==1) {
									dialogFlow.getChildren().clear();
									Text title = new Text("Hint!!");
									title.getStyleClass().add("wrongValueTitle");
									title.setFill(Color.RED);
									dialogFlow.getChildren().add(title);
								}
								wrong = (DefaultTextField)cellPane.getChildren().get(0);
								wrong.getHighlighter().highlightWrongCell(cellPane);
								
								/**fixing highlighting**/
								((CustomPane) cellPane).setChecked(true);
								checkedCells.add((CustomPane)cellPane);
								/***********************/
								
								
								Text message = new Text("\nmauvaise valeur: " + wrong.getText() 
								                        +" AT: row:" + wrong.getPositionX()
						                                +", column:" + wrong.getPositionY());
								message.getStyleClass().add("wrongValueMessage");
								
								
								dialogFlow.getChildren().add(message);
								iswrong = true;
							} 
						}
					}
				}
				
				
				
				if(!iswrong) {
					Move solution = null;
					
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
						 history.add(solution);
						 solution.act()
						 ;}
					
					/* if(solution==null) {
						 stop=true;
					 }*/
					 
					 if(solution!=null) {
						    dialogFlow.getChildren().clear();
						    Text title = new Text("Hint\n");
							title.setFill(Color.GREEN);
							title.getStyleClass().add("fullySolved");
							dialogFlow.getChildren().add(title);
							
							String s = solution.getDetails();
							 String[] array = s.split(":");
							 
							
							
							Text heuristique = new Text(array[0]+":");
							heuristique.setFill(Color.BLUE);
							heuristique.getStyleClass().add("heuristiqueName");
							
							Text hint = new Text(array[1]+"\n");
							hint.setFill(Color.BLACK);
							hint.getStyleClass().add("heuristiqueMessage");
							
							dialogFlow.getChildren().add(heuristique);
							dialogFlow.getChildren().add(hint);
							CustomPane p;
							for(Cell c: solution.getReasons()) {
								System.out.println("in");
								p = (CustomPane)cellPanes[c.getCoordinate().getX()][c.getCoordinate().getY()];
								p.getHighlighter().highlightReason(p);
							}
						 
					 }
				}
				
				 
				 
			}
			
		});
	    //Click sur edit
	    edit_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				CustomPane selectedPane = null;
				Node selectedChild;
				
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
				 valueTocandidates(selectedPane,true);
			}
	    	
	    });
	    //Click sur progress
	    checkProgress_Button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
                
				Node paneChild;
				int panePositionX;//customPane X positoion
				int panePositionY;//customPane Y position
				DefaultTextField cell;//TextField to check
				int count = 0;
				
				
				for(Pane[] array : cellPanes) {
					for(Pane cellPane: array) {
						paneChild = cellPane.getChildren().get(0); 
						if(paneChild instanceof DefaultTextField && ((DefaultTextField) paneChild).canEdit()) {
							panePositionX = ((CustomPane) cellPane).getPositionX();
							panePositionY = ((CustomPane) cellPane).getPositionY();
							if(!checker.Check(panePositionX, panePositionY)) {
								count++;
								if(count==1) {
									dialogFlow.getChildren().clear();
									Text title = new Text("Progress:");
									title.getStyleClass().add("ProgressTitle");
									title.setFill(Color.BLUE);
									dialogFlow.getChildren().add(title);
								}
								cell = (DefaultTextField)cellPane.getChildren().get(0);
								cell.getHighlighter().highlightWrongCell(cellPane);
								
								
							
								
								
								
								Text message = new Text("\nmauvaise valeur: " + cell.getText() 
								                        +" AT: row:" + cell.getPositionX()
						                                +", column:" + cell.getPositionY());
								message.getStyleClass().add("wrongValueMessage");
								
								
								dialogFlow.getChildren().add(message);
								
							} else {
								
								if(count==1) {
									dialogFlow.getChildren().clear();
									Text title = new Text("Progress:");
									title.getStyleClass().add("ProgressTitle");
									title.setFill(Color.BLUE);
									dialogFlow.getChildren().add(title);
								}
								
								cell = (DefaultTextField)cellPane.getChildren().get(0);
								cell.getHighlighter().highlightRightCell(cellPane);
								
								
								
								Text message = new Text("\nBonne valeur: " + cell.getText() 
								                        +" AT: row:" + cell.getPositionX()
						                                +", column:" + cell.getPositionY());
								message.getStyleClass().add("wrongValueMessage");
								
								
								dialogFlow.getChildren().add(message);
								
							}
							
							/**fixing highlighting**/
							((CustomPane) cellPane).setChecked(true);
							checkedCells.add((CustomPane)cellPane);
							/***********************/
						}
					}
				}
				
			}
	    	
	    });
	    //clique sur reset
	    resetButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Reset Confirmation");
				alert.setHeaderText("By reseting the grid you will loose all the progress you made ");
				alert.setContentText("Are you sure you want to reset the puzzle?");
                ButtonType confirm = new ButtonType("Confirm",ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("Cancel",ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(confirm,cancel);
                
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == confirm){
				    
					resetGrid();
					
				} else {
				    // ... user chose CANCEL or closed the dialog
				}
				
			}

			private void resetGrid() {
				grid = GridHelper.getInitialGrid();
				addGridChangeListeners();
				history.clearAll();

				
				candidateGrids.clear();
				CustomPane parent;
				CandidatTextField CTF;
				CandidatGridPane CGP;
				for(int i=0; i<9; i++) {
					for(int j=0; j<9; j++) {
						if(!grid.getCellAt(i, j).isLocked()) {
							parent = (CustomPane)cellPanes[i][j];
							parent.getChildren().clear();
							CTF = new CandidatTextField(500/9, 500/9);
							CGP = new CandidatGridPane();
							CGP.styleGridPane(500/9, 500/9);
							parent.definePaneParams(500/9, 500/9);
							parent.getChildren().add(CGP);
							parent.stylePane(i, j);
							cellPanes[i][j]=parent;
							CGP.setPositionX(i);
						 	CGP.setPositionY(j);
						 	CGP.addEventHandlers(cellPanes);
						 	//sudokuGrid.add(parent, j, i);
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
					 			
					 			CTF = new CandidatTextField(20,20);
					 			CTF.setBackground(Background.EMPTY);
					 			CTF.setEditable(false);
					 			/**********
					 			CTF.addEventHandlers(cellPanes);
					 			***************/
					 			addCandidatFieldListeners(CTF);
					 			 tempGrid.add(CTF, j, i);
					 			 if(grid.getCellAt(tempGrid.getPositionX(),tempGrid.getPositionY()).getCandidates().contains(""+sample[i][j])) {
					 				CTF.appendText(""+sample[i][j]);
					 				CTF.setFont(Font.font("Verdana", FontWeight.BOLD, 8));
					 				
					 			 }
								  
								  
						 	  }
				     }
				 }
				
				
				
				
				
				
				
			
			}
	    	
	    });
	        
	    //clique redo
	    redo_arrow.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
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
				
				
				history.goBackward();
				 
				
			}
				}
	    	
	    });
	
	    newGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				settings_HBOX.setVisible(!settings_HBOX.isVisible());
				return_button.setVisible(!return_button.isVisible());
				newGame_VBOX.setVisible(!newGame_VBOX.isVisible());
				closeSettings.setVisible(!closeSettings.isVisible());
				
			}
	    	
	    });
	    
	    return_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				return_button.setVisible(!return_button.isVisible());
				newGame_VBOX.setVisible(!newGame_VBOX.isVisible());
				settings_HBOX.setVisible(!settings_HBOX.isVisible());
				closeSettings.setVisible(!closeSettings.isVisible());
			}
	    	
	    });
	    
	    difficulty_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				return_button.setVisible(!return_button.isVisible());
				newGame_VBOX.setVisible(!newGame_VBOX.isVisible());
				return2.setVisible(!return2.isVisible());
				difficulty_VBOX.setVisible(!difficulty_VBOX.isVisible());
				startButton.setVisible(!startButton.isVisible());
			}
	    	
	    });
	    
	    return2.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				return_button.setVisible(!return_button.isVisible());
				newGame_VBOX.setVisible(!newGame_VBOX.isVisible());
				return2.setVisible(!return2.isVisible());
				difficulty_VBOX.setVisible(!difficulty_VBOX.isVisible());
				startButton.setVisible(!startButton.isVisible());
			}
	    	
	    });
	    
	    startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
				String toogleGroupValue = selectedRadioButton.getId();
				
				switch(toogleGroupValue) {
				   case "easyRadio" : grid = GridHelper.loadGridByDif(Difficulty.EASY); break;
				   
				   case "meduimRadio" : grid = GridHelper.loadGridByDif(Difficulty.MEDUIM);break;
				   
				   case "hardRadio" : grid = GridHelper.loadGridByDif(Difficulty.HARD);break;
				   
				}
				
				sudokuGrid.getChildren().clear();
				cells.clear();
				candidateGrids.clear();
				history.clearAll();
				checker = new CheckerFactory(grid);
				createGraphicGrid();
				addGridChangeListeners();
				optionListeners();
				settingStack.toFront();
				
			}
	    	
	    });
	    
	    import_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				fileChooser.getExtensionFilters().add(new ExtensionFilter("xml file","*.xml"));
				File f = fileChooser.showOpenDialog(null);

				if(f != null) {
					String s = f.getPath();
					grid = GridHelper.loadGrid(s);
					sudokuGrid.getChildren().clear();
					cells.clear();
					candidateGrids.clear();
					history.clearAll();
					checker = new CheckerFactory(grid);
					createGraphicGrid();
					addGridChangeListeners();
					optionListeners();
					settingStack.toFront();
					
				}
			}
	    	
	    });
	    
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
 	
	
	
	@FXML
    void showSettingsMenu(MouseEvent event) {
		ShowParamMenu(event);
    }
	
	
void ShowParamMenu(MouseEvent event) {
        
		TranslateTransition transition = new TranslateTransition();
		transition.setDuration(Duration.seconds(0.5));
		transition.setNode(settingStack);
		settingStack.toFront();
		if(ishiden) {
			
			transition.setToY(settingStack.getLayoutY()-405);
		}
		else {
			
			transition.setToY(660);
		}
		transition.play();
		ishiden = !ishiden;
    }

public void valueTocandidates(CustomPane selectedPane,boolean act) {
	//CustomPane selectedPane = null;
	Node selectedChild;
	
	
	
	if(selectedPane != null) {
	   selectedChild = selectedPane.getChildren().get(0);
	   if(selectedChild instanceof DefaultTextField) {
		   DefaultTextField textField = (DefaultTextField) selectedChild;
		   if(textField.getModel().canEdit() || !act) {
	 			//dtf.getModel().setCandidats(grid.getCellAt(dtf.getPositionX(),dtf.getPositionY()).getCandidates());
            	 selectedPane = (CustomPane)textField.getParent();
				CandidatGridPane gridPane = new  CandidatGridPane();
				int[][]sample= {{1,2,3},{4,5,6},{7,8,9}};
				gridPane.styleGridPane(0, 0);
				CandidatGridPane existing;
				if(act) {
				Move move = new Move();
				Cell cc =grid.getCellAt(textField.getPositionX(),textField.getPositionY());
				move.addAction(Move.REMOVE_VALUE, cc, cc.getValue());
				move.act();
				history.add(move);
				}
				
				//grid.getCellAt(dtf.getPositionX(),dtf.getPositionY()).setValue("");
				
				
				for(int i=0; i<9;i++) {
					grid.getCellAt(textField.getPositionX(), i).setCandidates(grid.getPossibleCandidat(textField.getPositionX(), i));
					grid.getCellAt(i,textField.getPositionY()).setCandidates(grid.getPossibleCandidat(i,textField.getPositionY()));
					if(i!=textField.getPositionX() || i!=textField.getPositionY()) {
						if((cellPanes[textField.getPositionX()][i] instanceof CandidatGridPane)) {
							existing = (CandidatGridPane) cellPanes[textField.getPositionX()][i].getChildren().get(0);
							//existing.appendCandidat(dtf.getText());
						}
						
						if((cellPanes[textField.getPositionY()][i] instanceof CandidatGridPane)) {
							existing = (CandidatGridPane) cellPanes[i][textField.getPositionY()].getChildren().get(0);
							//existing.appendCandidat(dtf.getText());
						}
					}
				}
				
				for(Cell c : grid.getRegion(textField.getPositionX(), textField.getPositionY())) {
					c.setCandidates(grid.getPossibleCandidat(c.getCoordinate().getX(), c.getCoordinate().getY()));
					if((cellPanes[c.getCoordinate().getX()][c.getCoordinate().getY()] instanceof CandidatGridPane)) {
						existing = (CandidatGridPane) cellPanes[c.getCoordinate().getX()][c.getCoordinate().getY()].getChildren().get(0);
						//existing.appendCandidat(dtf.getText());
					}
				}
			
				for(int j=0; j<3;j++) {
					for(int i=0; i<3;i++) {
						//CandidatPane CP = new CandidatPane();
						CandidatTextField CTF = new CandidatTextField(20,20);
						CTF.setBackground(Background.EMPTY);
						CTF.setEditable(false);
						addCandidatFieldListeners(CTF);
						if(grid.getCellAt(textField.getPositionX(),textField.getPositionY()).getCandidates().contains(""+sample[j][i])) {
							CTF.appendText(""+sample[j][i]);
							CTF.setFont(Font.font("Verdana", FontWeight.BOLD, 8));
						}
						//CP.getChildren().add(CTF);
						gridPane.add(CTF, i, j);
					}
				}
				gridPane.setPositionX(selectedPane.getPositionX());
				gridPane.setPositionY(selectedPane.getPositionY());
	 			gridPane.addEventHandlers(cellPanes);
            	selectedPane.getChildren().clear();
				selectedPane.getChildren().add(gridPane);
			}
	   }
	}
}

public void candidatesToValue(CustomPane selectedPane ,String value) {
	
	int X = selectedPane.getPositionX();
	int Y = selectedPane.getPositionY();
	cellPanes[X][Y].getChildren().clear();
    
    DefaultTextField DTF = new DefaultTextField(500/9,500/9);	
	DTF.setCanEdit(true);
	addDefaultFieldListeners(DTF);
	DTF.setPositionX(X);
	DTF.setPositionY(Y);
	
	DTF.appendText(value);
	DTF.setStyle("-fx-text-fill: #fa8132;");
	 cellPanes[X][Y].getChildren().add(DTF);
}

}