package components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import componentsModel.PaneModel;
import javafx.scene.layout.Pane;

public abstract class CustomPane extends Pane{

	private int positionX;
	private int positionY;
	private boolean selected;
	private PaneModel model;
	private SelectHighlighter highlighter;
	private boolean checked = false;
	
	public CustomPane() {
		super();
		highlighter = new SelectHighlighter();
		model = new PaneModel();
		model.addPropertyChangeListener("SELECTED", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				if(((boolean)arg0.getNewValue())==true) {
					highlighter.highLightCell(getPane());
				}
			}
			
		});
	}
	
	
	
    public String stylePane(int X, int Y) {
    	if(Y==2||Y==5) {
			
			  if(X==2||X==5) {
				  this.setStyle("-fx-border-width:1 2 2 1; -fx-border-color: black #fa8132 #fa8132 black;");
			  } else if(X==3||X==6) {
				  this.setStyle("-fx-border-width:2 2 1 1; -fx-border-color: #fa8132 #fa8132 black black;");
			  }else {
				  this.setStyle("-fx-border-width:1 2 1 1; -fx-border-color: black #fa8132 black black;");
			  }
		  }else if(Y==3||Y==6) {
			
			  if(X==2||X==5) {
				  this.setStyle("-fx-border-width:1 1 2 2; -fx-border-color: black black #fa8132 #fa8132;");
			  } else if(X==3||X==6) {
				  this.setStyle("-fx-border-width:2 1 1 2; -fx-border-color: #fa8132 black black #fa8132;");
			  }else {
				  this.setStyle("-fx-border-width:1 1 1 2; -fx-border-color: black black black #fa8132;");
			  }
			  
		  } else {
			  if(X==2||X==5) {
				  this.setStyle("-fx-border-width:1 1 2 1; -fx-border-color: black black #fa8132 black;");
			  } else if(X==3||X==6) {
				  this.setStyle("-fx-border-width:2 1 1 1; -fx-border-color: #fa8132 black black black;");
			  }else {
				  this.setStyle("-fx-border-width:1; -fx-border-color: black;");
			  }
		  }
    	return this.getStyle();
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
	
	public abstract void definePaneParams(double height, double width);



	public boolean isSelected() {
		return selected;
	}



	/*public void setSelected(boolean selected) {
		this.selected = selected;
	}*/
	
	public PaneModel getModel() {
		return model;
	}
	
	public CustomPane getPane() {
		return this;
	}
	
	public SelectHighlighter getHighlighter() {
		return highlighter;
	}



	public boolean isChecked() {
		return checked;
	}



	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
