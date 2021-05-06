package components;

import javafx.scene.layout.Pane;

public class DefaultGridPane extends CustomGridPane {

	
	public DefaultGridPane() {
		super();
	}
	
	
	@Override
	public void styleGridPane(double width,double height) {
		// TODO Auto-generated method stub
		this.setGridLinesVisible(true);
		this.setPrefWidth(width);
		this.setPrefHeight(height);
		this.setLayoutX(34);
		this.setLayoutY(76);
	}

	@Override
	public void addEventHandlers(Pane[][] p) {
		// TODO Auto-generated method stub
		
	}

}
