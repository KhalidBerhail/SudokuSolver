package components;

import javafx.scene.layout.Pane;

public interface Highlighter {

	
	public void highLightCell(Pane P);
	public void highLightCellHovered(Pane P);
	public void undoCellHighLight(Pane P);
	
	public void highLightLines(Pane[] P);
	public void undoLinesHighLight(Pane[] P);
	
	public void highLightColumns(Pane[] P);
	public void undoColumnsHighLight(Pane[] P);
	
	public void highLightArea(Pane[][] P);
	public void undoAreaHighLight(Pane[][] P);
	
	public void highLightSelection(int X, int Y);
	public void undoSelectionHighLight(int X, int Y);
	
	public void undoCellSelections();
}
