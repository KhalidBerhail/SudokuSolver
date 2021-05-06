package components;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public abstract class CustomGridPane extends GridPane{
    private int positionX;
    private int positionY;
    
	public CustomGridPane() {
		super();
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	
	public void defineGridConstraints(int nbrLines,int nbrCollumns) {
		 	
		          ColumnConstraints cc = new ColumnConstraints();
		          cc.setFillWidth(true);
		          cc.setHgrow(Priority.ALWAYS);
		          this.getColumnConstraints().add(cc);
		 	
		          RowConstraints rc = new RowConstraints();
		          rc.setFillHeight(true);
		          rc.setVgrow(Priority.ALWAYS);
		          this.getRowConstraints().add(rc);
		      
	 }
	
	public Node getGridCell(int col, int row) {
	    for (Node node : this.getChildren()) {
	    	if(GridPane.getColumnIndex(node) != null&& GridPane.getRowIndex(node) != null) {
	    		if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
		            return node;
		        }
	    	}
	        
	    }
	    return null;
	}
	
	
	public abstract void styleGridPane(double width,double height);
	public abstract void addEventHandlers(Pane[][] p);
	
}
