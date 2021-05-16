package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;
import java.util.TreeSet;

import helpers.Contract;

public class StdCell implements Cell {
	
	// ATTRIBUTS
	
	private Grid currentGrid;
	private Coordinate coord;
	private boolean isLocked;	
	private String currentValue;
	private Set<String> candidates;
	private PropertyChangeSupport pcs;
	
	// CONSTRUCTEURS
	
	public StdCell(Grid grid, Coordinate coord) {
		Contract.checkWrongCondition(grid==null,"Null Grid ");
		this.currentGrid = grid;
		this.currentValue = null;
		this.coord = coord;
		this.candidates = new TreeSet<String>(StdGrid.defaultValueSet());
		pcs = new PropertyChangeSupport(this);
		isLocked = false;
	}
	public StdCell(Coordinate coord) {
		
		this.currentValue = null;
		this.coord = coord;
		this.candidates = new TreeSet<String>();
		pcs = new PropertyChangeSupport(this);
		isLocked = false;
	}
	public StdCell(Cell cell) {
		this.currentValue = cell.getValue().toString();
		this.coord = cell.getCoordinate();
		this.candidates = cell.getCandidates();
		this.currentGrid = cell.getGrid();
		pcs = new PropertyChangeSupport(this);
		isLocked = cell.isLocked();
	}
	
	// REQUETES

	@Override
	public boolean isPossible(String value) {
		return !isLocked;
	}
	
	@Override
	public boolean isLocked() {
		return isLocked;
	}

	@Override
	public String getValue() {
		return this.currentValue;
	}
	

	@Override
	public String toString() {
		StringBuffer value = new StringBuffer();
		value.append((currentValue == null ?
			"( X ;" :
			"( Value: " + getValue().toString() + ";"));
		if (this.getCandidates() != null && this.getCandidates().size() > 0) {
			Object[] tab = getCandidates().toArray();
			value.append(tab[0].toString());
			for (int i = 1; i < tab.length; ++i) {
				value.append("," + tab[i].toString());
			}
		} else {
			value.append(" " );
		}
		value.append(  this.coord.toString());
		value.append(")");
		return value.toString();
	}
	@Override
	public Coordinate getCoordinate() {
		return this.coord;
	}
	

	@Override
	public Grid getGrid() {
		return this.currentGrid;
	}

	@Override
	public void setGrid(Grid g) {
		// TODO Auto-generated method stub
		currentGrid=g;
	}
	@Override
	public Set<String> getCandidates() {
		
		//return (getValue() != null ? null : candidates);
		return candidates;
	}

	
	@Override
	public void lock() {
		if (!isLocked()) {
			isLocked = true;
			pcs.firePropertyChange(PROP_LOCKED, false, true);
		}
	}
	
	@Override
	public void unLock() {
		if (isLocked()) {
			isLocked = false;
			pcs.firePropertyChange(PROP_LOCKED, true, false);
		}
	}

	@Override
	public void setValue(String value) {
		if (!isLocked) {
			String oldValue = this.currentValue;
			this.currentValue = value;
			pcs.firePropertyChange("VALUE_DEFINED", 
					new PropertyChangeCell(this, oldValue, null), 
					new PropertyChangeCell(this, value, null));
		}
	}

	@Override
	public void removeValue() {
		
		if (isLocked) {
			
			if(currentValue!=null) {
				char c = currentValue.charAt(0);
				String oldValue= ""+c;
				this.currentValue = null;
				pcs.firePropertyChange("VALUE_REMOVED", 
						new PropertyChangeCell(this, oldValue, null), 
						new PropertyChangeCell(this, null, null));
			}
			
		}
	}

	@Override
	public void addCandidate(String value) {
		if (!isLocked) {
			if (value == null) {
				throw new AssertionError("addCandidate null: StdCell");
			}
			boolean contains = candidates.contains(value);
			this.candidates.add(value);
			pcs.firePropertyChange("CANDIDAT_ADDED", 
					new PropertyChangeCell(this,null,null), 
					new PropertyChangeCell(this, null, value));
		}
	}

	@Override
	public void eliminateCandidate(String value) {
		if (!isLocked) {
			if (value == null) {
				throw new AssertionError("eliminateCandidate null: StdCell");
			}
			boolean contains = candidates.contains(value);
			this.candidates.remove(value);
			pcs.firePropertyChange("CANDIDAT_ELIMINATED", 
					new PropertyChangeCell(this, null, null), 
					new PropertyChangeCell(this, this.getValue(), value));
		}
	}

	
	@Override
	public void addPropertyChangeListener(String pName, PropertyChangeListener listener) {
		if (listener != null) {
			pcs.addPropertyChangeListener(pName, listener);
		}
	}

	@Override
	public void removePropertyChangeListener(String pName, PropertyChangeListener listener) {
		if (listener != null) {
			pcs.removePropertyChangeListener(pName, listener);
		}
	}
	
	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return pcs.getPropertyChangeListeners();
	}
	
	@Override
	public PropertyChangeListener[] getPropertyChangeListeners(String name) {
		return pcs.getPropertyChangeListeners(name);
	}
	
	public class PropertyChangeCell {
		private Cell cell;
		private String value;
		private String candidate;
		
		private PropertyChangeCell(Cell _cell, String _value, String _candidate) {
			cell = _cell;
			value = _value;
			candidate = _candidate;
		}
		
		public Cell getCell() {
			return cell;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCandidate() {
			return candidate;
		}
	}

	@Override
	public void setCandidates(Set<String> candidats) {
		candidates=candidats;
	}



}
