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
	}

	@Override
	public void addEventHandlers(Pane[][] P) {}

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
