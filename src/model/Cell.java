package model;

import java.beans.PropertyChangeListener;
import java.util.Set;

public interface Cell {

	public static final String PROP_VALUE = "currentValue";
	public static final String PROP_CANDIDATES = "candidates";
	public static final String PROP_LOCKED = "locked";
	
	// REQUETES
	
	
	boolean isPossible(String value);
	
	boolean isLocked();
	
	
	String getValue();
	
	String toString();
	
	
	Coordinate getCoordinate();

	Grid getGrid();
	
	
	Set<String> getCandidates();
	void setCandidates(Set<String> candidats);
	
	// COMMANDES
	
	void lock();

	void unLock();
	
	void setGrid(Grid g);
	void setValue(String value);
	
	void removeValue();
	

	void addCandidate(String value);


	void eliminateCandidate(String value);
	
	// LISTENERS

	void addPropertyChangeListener(String pName, PropertyChangeListener listener);

	
	void removePropertyChangeListener(String pName, PropertyChangeListener listener);


	PropertyChangeListener[] getPropertyChangeListeners();
	

	PropertyChangeListener[] getPropertyChangeListeners(String name);
	 
}