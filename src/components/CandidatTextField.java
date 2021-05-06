package components;

/*import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;*/
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class CandidatTextField extends CustomTextField {

	private SelectHighlighter highlighter;
	
	public CandidatTextField(double width, double height) {
		super(width, height);
		highlighter = new SelectHighlighter();
	}

	@Override
	public void addEventHandlers(Pane[][] P) {/*
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
			   @Override
				public void handle(MouseEvent arg0) {
				   setCursor(Cursor.DEFAULT);
				}
			});
			
			this.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					CustomPane pane;
					boolean selected;
					pane = (CustomPane)getParent().getParent();
					selected = pane.getModel().isSelected();
					
					if(selected) {
						if(!getText().equals("")) {
							DefaultTextField candidat = new DefaultTextField(500/9,500/9);	
							candidat.setCanEdit(true);
							candidat.addEventHandlers(P);
							candidat.setPositionX(pane.getPositionX());
							candidat.setPositionY(pane.getPositionY());
							candidat.appendText(getText());
							candidat.getModel().setCanEdit(true);
							pane.getChildren().clear();
							pane.getChildren().add(candidat);
							pane.getChildren().get(0).setStyle("-fx-text-fill: #fa8132;");
							pane.getModel().setSelected(true);
						}
					}
					else {
							highlighter.undoCellSelections(P);
							highlighter.highLightSelection(pane.getPositionX(),pane.getPositionY(),P);
							pane.getModel().setSelected(true);
						
					}
					
					
				}
				
			});
	*/}

	@Override
	public void styleTextField() {
		// TODO Auto-generated method stub
		
	}
	
	public SelectHighlighter getHighlighter() {
		return highlighter;
	}

}
