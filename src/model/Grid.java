package model;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Set;






public interface Grid  {
	
	static final int REGION_SIZE = 3;
	 static final int size = 9;
	
	
	 StdGrid deepCopy(Grid grid);
	
	void setGrid(Cell[][] grille);
	
    void generateAllCandidat();
	
	Cell getCellAt(int x, int y);

	Coordinate getCoordinate(Cell cell);
	
	Collection<Cell> getCells();
	
	String getValueFrom(int x, int y);
	
	Set<String> getCandidatesFrom(int x, int y);
	
	
	Cell[][] getGrid();
	
	Set<Cell> getColumn(int y);
	
	Set<Cell> getRegion(int x,int y);
	
	String[][] getSolutionString();
	
	String[][] getStringGrid();
	
	
	// COMMANDES
	
	boolean checkSolution() ;
	
	
	// LISTENERS

	void addPropertyChangeListenerGrid(String pName, PropertyChangeListener listener);

	void removePropertyChangeListenerGrid(String pName, PropertyChangeListener listener);
	
	void addPropertyChangeListenerCell(String pName, PropertyChangeListener listener,
			int x, int y);
	
	void removePropertyChangeListenerCell(String pName, PropertyChangeListener listener,
			int x, int y);
	
	PropertyChangeListener[] getPropertyChangeListenerCell(int x, int y);
	
	void copySharedPCL(Grid newGrid);
	
	
	PropertyChangeListener[] getPropertyChangeListenerShared();
	void addPropertyChangeListener(String pName, PropertyChangeListener listener);
	

	


	Set<String> getPossibleCandidat(int x, int y);
}
