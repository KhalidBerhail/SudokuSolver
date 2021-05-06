package components;

import javafx.scene.layout.Pane;

public class SelectHighlighter implements Highlighter {

	private Pane[][] cellPanes;
	
	public void attributePanes(Pane[][] P) {
		cellPanes = P;
	}
	
	public Pane[][] getPanes() {
		return cellPanes;
	}
	@Override
	public void highLightCell(Pane P) {
		// TODO Auto-generated method stub
		CustomPane pane;
	    pane = (CustomPane)P;
	    if(pane.getModel().isSelected()) {
	    	P.setStyle("-fx-background-color:#FFDF00;"+pane.stylePane(pane.getPositionX(), pane.getPositionY()));
	    }
		
	}
	
	@Override
	public void highLightCellHovered(Pane P) {
		// TODO Auto-generated method stub
		CustomPane pane;
	    pane = (CustomPane)P;
	    if(!pane.getModel().isSelected()) {
	    	P.setStyle("-fx-background-color:#CDCDCD;"+pane.stylePane(pane.getPositionX(), pane.getPositionY()));
	    }
		
	}

	@Override
	public void highLightLines(Pane[] P) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void highLightColumns(Pane[] P) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void highLightArea(Pane[][] P) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void highLightSelection(int X, int Y) {
		int row=(X/3)*3;
        int col=(Y/3)*3;
        CustomPane pane ;
        
        
		for(int x = 0; x < 9 ; x++) {
			
			pane = (CustomPane)cellPanes[x][Y];
			if(!pane.getModel().isSelected()) {
				pane.setStyle(cellPanes[x][Y].getStyle()+"-fx-background-color: #CDCDCD;");
				pane.getModel().setHovered(true);
			}
			pane = (CustomPane)cellPanes[X][x];
			//pane = (CustomPane)P[X][x];
			if(!pane.getModel().isSelected()) {
				pane.setStyle(cellPanes[X][x].getStyle()+"-fx-background-color: #CDCDCD;");
				pane.getModel().setHovered(true);
			}
			
		}
		
		for(int x=0;x<3;x++) {
            for(int y=0;y<3;y++) {
    			pane = (CustomPane)cellPanes[row+x][col+y];
    			if(!pane.getModel().isSelected()) {
    				cellPanes[row+x][col+y].setStyle(cellPanes[row+x][col+y].getStyle()+"-fx-background-color:#CDCDCD;");
    				pane.getModel().setHovered(true);
    			}
            	
            }
        }
	}

	@Override
	public void undoCellHighLight(Pane P) {
		// TODO Auto-generated method stub
		CustomPane pane;
	    pane = (CustomPane)P;
		if(!pane.getModel().isSelected() && !pane.getModel().isHovered()) {
			pane.stylePane(pane.getPositionX(), pane.getPositionY());
		}
	}

	@Override
	public void undoLinesHighLight(Pane[] P) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void undoColumnsHighLight(Pane[] P) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void undoAreaHighLight(Pane[][] P) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void undoSelectionHighLight(int X, int Y) {
		
		CustomPane pane ;
	       for(int x = 0; x < 9 ; x++) {
				for(int y = 0; y <9; y++) {
				    pane = (CustomPane)cellPanes[x][y];
					if(!pane.getModel().isSelected()) {		
					   pane.stylePane(x,y);
					   pane.getModel().setHovered(false);
					}
			}
		}
		
	}

	@Override
	public void undoCellSelections() {
		// TODO Auto-generated method stub
		CustomPane pane;
		for(int x = 0; x < 9 ; x++) {
			for(int y = 0; y <9; y++) {
				pane = (CustomPane)cellPanes[x][y];
				if(pane.getModel().isSelected()) {
					pane.stylePane(x,y);
					pane.getModel().setSelected(false);
					pane.getModel().setHovered(false);
				}
			}
			
		}
	}

}
