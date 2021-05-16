package components;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class CandidatGridPane extends CustomGridPane {

	private SelectHighlighter highlighter;
	public CandidatGridPane() {
		super();
		highlighter = new SelectHighlighter();
	}
	
	
	@Override
	public void styleGridPane(double width,double height) {
		// TODO Auto-generated method stub
		this.setPrefSize(500/9,500/9);
	}

	@Override
	public void addEventHandlers(Pane[][] p) {
		// TODO Auto-generated method stub
		this.highlighter.attributePanes(p);
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
	   		@Override
	   		public void handle(MouseEvent arg0) {
	   			// TODO Auto-generated method stub
	   			
	   			setCursor(Cursor.DEFAULT);
	   			/************OLD HIGHLIGHTER
	   			 * highlighter.highLightSelection(getPositionX(),getPositionY());
	   			 */
	   			highlighter.highLightCellHovered((Pane)getParent());
	   		}
	   	   });
		
		this.setOnMouseExited(new EventHandler<MouseEvent>() {

	   		@Override
	   		public void handle(MouseEvent arg0) {
	   			// TODO Auto-generated method stub
	   		   
	   			/*********OLD HIGHLIGHTER
	   			 * highlighter.undoSelectionHighLight(getPositionX(),getPositionY());
	   			 */
	   			highlighter.undoCellHighLight((Pane)getParent());
	   		}
	   		   
	   	   });
	}
	
	public void appendCandidat(String s) {
		int x = Integer.parseInt(s);
		CandidatTextField ctf;
		CandidatGridPane gp;
		int line;
		int col;
		if(x%3!=0) {
			line = x/3;
			col = x%3 - 1;
			
		
		}else {
			line = x/3 - 1;
			col = 2;
		}
		
		ctf = (CandidatTextField)this.getGridCell(col, line);
		ctf.clear();
		ctf.appendText(s);
		
	}
	public void clearnCandidat(String s) {
		int x = Integer.parseInt(s);
		CandidatTextField ctf;
		CandidatGridPane gp;
		int line;
		int col;
		if(x%3!=0) {
			line = x/3;
			col = x%3 - 1;
			
		
		}else {
			line = x/3 - 1;
			col = 2;
		}
		
		ctf = (CandidatTextField)this.getGridCell(col, line);
		ctf.clear();
		
	}
	
	public CandidatTextField getGridCell(int col, int row) {
	    for (Node node : this.getChildren()) {
	    	if(GridPane.getColumnIndex(node) != null&& GridPane.getRowIndex(node) != null) {
	    		if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
		            return (CandidatTextField)node;
		        }
	    	}
	        
	    }
	    return null;
	}

}
