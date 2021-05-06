package components;

import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public abstract class CustomTextField extends TextField {

	private int positionX;
	private int positionY;
	private String appendable;
	private Font custumFont = Font.loadFont(this.getClass().getResource("/resources/OrangeJuice.ttf").toExternalForm(),31);
    
	
	public CustomTextField(double width, double height) {
    	super();
    	this.setMinSize(0,0);
 	   this.setMaxSize(width,height);
 	   this.setPrefSize(width, height);
 	   this.setEditable(false);
 	   this.setFont(custumFont);
 	   this.setBackground(Background.EMPTY);
 	   
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
	public String getAppendable() {
		return appendable;
	}
	public void setAppendable(String appendable) {
		if(appendable!=null) {
			this.appendable = appendable;
		}
	}
	
	
	public abstract void addEventHandlers(Pane[][] P);
	public abstract void styleTextField();
}
