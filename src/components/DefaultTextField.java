package components;

/*import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;*/


import javafx.scene.layout.Pane;
import componentsModel.TextFieldModel;


public class DefaultTextField extends CustomTextField {

	private SelectHighlighter highlighter;
	private boolean canEdit;
	private TextFieldModel model;
	
	public DefaultTextField(double width, double height) {
		
		super(width, height);
		highlighter = new SelectHighlighter();
		canEdit = false;
		model = new TextFieldModel();
		/*model.addPropertyChangeListener("CANDIDATS", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				CustomPane pane = (CustomPane)getParent();
				CandidatGridPane gridPane = new  CandidatGridPane();
				int[][]sample= {{1,2,3},{4,5,6},{7,8,9}};
				gridPane.styleGridPane(0, 0);
				for(int j=0; j<3;j++) {
					for(int i=0; i<3;i++) {
						CandidatTextField CTF = new CandidatTextField(20,20);
						CTF.setBackground(Background.EMPTY);
						CTF.setEditable(false);
						if(((Set<String>)arg0.getNewValue()).contains(""+sample[j][i])) {
							CTF.appendText(""+sample[j][i]);
							CTF.setFont(Font.font("Verdana", FontWeight.BOLD, 8));
						}
						gridPane.add(CTF, i, j);
					}
				}
	 			
            	pane.getChildren().clear();
				pane.getChildren().add(gridPane);
	 			
	 			
			}
		});*/
	}

	@Override
	public void addEventHandlers(Pane[][] P) {/* 
		
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
	   		@Override
	   		public void handle(MouseEvent arg0) {
	   			setCursor(Cursor.DEFAULT);
	   			highlighter.highLightSelection(getPositionX(),getPositionY(),P);
	   		}	   	
	   	});
		
		this.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				highlighter.undoSelectionHighLight(getPositionX(),getPositionY(),P);
			}
		});
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				CustomPane pane = (CustomPane)getParent();
				boolean selected = pane.getModel().isSelected();
				if(!selected) {
					
					highlighter.undoCellSelections(P);
					highlighter.highLightSelection(pane.getPositionX(),pane.getPositionY(),P);
					pane.getModel().setSelected(true);
				} else {
                    if(model.canEdit()) {
					    Set<String> cnd = new TreeSet<String>();
        	 			cnd.add("5");
        	 			cnd.add("7");
        	 			cnd.add("9");
        	 			model.setCandidats((TreeSet<String>)cnd);
					}
				}
			}
			
		});
		
		
	*/}

	@Override
	public void styleTextField() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean canEdit() {
		return canEdit;
	}
	
	public void setCanEdit(boolean x) {
		canEdit = x;
	}

	public TextFieldModel getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public SelectHighlighter getHighlighter() {
		return highlighter;
	}

}
