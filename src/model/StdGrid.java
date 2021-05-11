package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import helpers.Contract;


public class StdGrid  implements Grid {
	
	// ATTRIBUTS


	private Set<PropertyChangeListener> spcl;
	private Map<PropertyChangeListener, String> spclNames;
	private Cell[][] cells;
	private PropertyChangeSupport pcs;


	// CONSTRUCTEURS
	
	public StdGrid() {
		
		
		
		this.cells = new Cell[size][size];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				this.cells[i][j] = new StdCell(this, new StdCoordinate(i, j));
			}
		}
		spcl = new HashSet<PropertyChangeListener>();
		spclNames = new HashMap<PropertyChangeListener, String>();
        pcs = new PropertyChangeSupport(this);
	}
	
	
	public StdGrid(Set<String> set, Cell[][] grille) {
		this();
		cells=grille;
		
	}
	
	
	
	
	
	
	// REQUETES
	


	@Override
	public void setGrid(Cell[][] grille) {
		// TODO Auto-generated method stub
		this.cells=grille;
	}
	@Override
	public StdGrid deepCopy(Grid grid) {
		// TODO Auto-generated method stub
		StdGrid copy = new StdGrid();
		Cell[][] cellsCopy = new Cell[size][size];
		

		Coordinate coor ;
		for(Cell[] c:cells) {
			for(Cell cc : c) {
				coor = cc.getCoordinate();
				
				cellsCopy[coor.getX()][coor.getY()] =new  StdCell( new StdCoordinate(coor.getX(),coor.getY()));
				cellsCopy[coor.getX()][coor.getY()].setCandidates( new HashSet<>(cc.getCandidates()));
				cellsCopy[coor.getX()][coor.getY()].setValue(cc.getValue());
				if(cc.isLocked()) cellsCopy[coor.getX()][coor.getY()].lock();
				else cellsCopy[coor.getX()][coor.getY()].unLock();
				cellsCopy[coor.getX()][coor.getY()].setGrid(copy);
				
				 
								
			}
		}
		copy.setGrid(cellsCopy);
		return copy;
	}
	
	@Override
	public Cell getCellAt(int x, int y) {
		Contract.checkWrongCondition(!checkCoordinate(x, y),"not valid coordinate");
		return cells[x][y];
	}

	@Override
	public Coordinate getCoordinate(Cell cell) {
		return cell.getCoordinate();
	}

	

	@Override
	public Collection<Cell> getCells() {
		Collection<Cell> c = new ArrayList<Cell>();
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				c.add(cells[i][j]);
			}
		}
		return c;
	}
	
	@Override
	public Set<Cell> getColumn(int y){
		Set<Cell> columnElements =new HashSet<Cell>();
		for(int i=0;i<size;i++) {
			columnElements.add(this.getCellAt(i,y));
		}
		return columnElements;
	}
	@Override
	public Set<Cell> getRegion(int x,int y){
		Set<Cell> regionElements =new HashSet<Cell>();
		 int currentRow = (x / REGION_SIZE) * REGION_SIZE;
		   int currentCol = (y / REGION_SIZE) * REGION_SIZE;
		    for (int r = 0; r < REGION_SIZE; r++) {
		      for (int c = 0; c < REGION_SIZE; c++) {
		    	  regionElements.add(this.getCellAt(currentRow + r,currentCol + c)); 
		        
		      }
		    }
		
		return regionElements;
	}
	@Override
	public String getValueFrom(int x, int y) {
		Contract.checkWrongCondition(!checkCoordinate(x, y),"not valid coordinate");
		return cells[x][y].getValue();
	}
	
	@Override
	public Set<String> getCandidatesFrom(int x, int y) {
		Contract.checkWrongCondition(!checkCoordinate(x, y),"not valid coordinate");
		return cells[x][y].getCandidates();
	}
	
	@Override
	public String[][] getSolutionString() {
		String[][] sol = new String[size][size];
		for (int i = 0; i < size ; ++i) {
			for (int j = 0; j < size ; ++j) {
				sol[i][j] = cells[i][j].getValue();
			}
		}
		// return this.getSolution();
		return sol; 
	}
	
	@Override
	public String[][] getStringGrid() {
		String [][] array = new String[size][size];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				array[i][j] = cells[i][j].getValue();
			}
		}
		return array;
	}

	
	
	
	
	// PRIVATE REQUEST

	
	
	private boolean checkCoordinate(int x, int y) {
		return !((x < 0 || x >= size) &&
				(y < 0 || y >= size));
	}
	
	// COMMANDS
	
	

	
	


	
	// STATIC METHODS
	
	
	public static Set<String> defaultValueSet() {
		Set<String> values = new HashSet<String>();
		for (int i = 1; i <= size; i++) {
			values.add(Integer.toString(i));
		}
		return values;
	}
	
	// LISTENERS
	
	public void addPropertyChangeListenerGrid(String pName, PropertyChangeListener listener) {
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				cells[i][j].addPropertyChangeListener(pName, listener);
			}
		}
		spcl.add(listener);
		spclNames.put(listener, pName);
	}
	
	public void removePropertyChangeListenerGrid(String pName, PropertyChangeListener listener) {
		for (int i = 0; i <  size; ++i) {
			for (int j = 0; j < size; ++j) {
				cells[i][j].removePropertyChangeListener(pName, listener);
			}
		}
		spcl.remove(listener);
		spclNames.remove(listener);
	}
	
	public void addPropertyChangeListenerCell(String pName, PropertyChangeListener listener,
			int x, int y) {
		Contract.checkWrongCondition(!checkCoordinate(x,y),"not valid coordinate");	
		cells[x][y].addPropertyChangeListener(pName, listener);
	}
	
	public void removePropertyChangeListenerCell(String pName, PropertyChangeListener listener,
			int x, int y) {
		Contract.checkWrongCondition(!checkCoordinate(x,y),"not valid coordinate");
		cells[x][y].removePropertyChangeListener(pName, listener);
	}
	
	public PropertyChangeListener[] getPropertyChangeListenerCell(int x, int y) {
		Contract.checkWrongCondition(!checkCoordinate(x,y),"not valid coordinate");	
		return cells[x][y].getPropertyChangeListeners();
	}
	
	public PropertyChangeListener[] getPropertyChangeListenerShared() {
		return (PropertyChangeListener[]) spcl.toArray();
	}
	
	@Override
	public void copySharedPCL(Grid newGrid) {
		Contract.checkWrongCondition(newGrid == null);
		for (PropertyChangeListener pcl : spcl) {
			newGrid.addPropertyChangeListenerGrid(spclNames.get(pcl), pcl);
		}
	}


	
	

	
	
	@Override
	public Set<String> getPossibleCandidat(int x ,int y) {
		// TODO Auto-generated method stub
		Set<String> candidat = new TreeSet<String>();
		Coordinate coordiante =new StdCoordinate(x, y);
		
		
    	if(!this.getCellAt(x, y).isLocked() ) {
    		for (int i = 1; i < size+1; i++) {
    			candidat.add(Integer.toString(i));
    		}
    	for(int i=0;i<size;i++) {
    		if(cells[coordiante.getX()][i].getValue()!=null && candidat.contains(cells[coordiante.getX()][i].getValue())){
    			candidat.remove(cells[coordiante.getX()][i].getValue());
    		}
    	}
    	for(int i=0;i<size;i++) {
    		if(cells[i][coordiante.getY()].getValue()!=null && candidat.contains(cells[i][coordiante.getY()].getValue())){
    			candidat.remove(cells[i][coordiante.getY()].getValue());
    		}
    	}
    	int _row=(coordiante.getX()/REGION_SIZE)*REGION_SIZE;
        int _col=(coordiante.getY()/REGION_SIZE)*REGION_SIZE;
        for(int i=0;i<REGION_SIZE;i++) {
            for(int j=0;j<REGION_SIZE;j++) {
            	if(cells[_row+i][_col+j].getValue()!=null &&candidat.contains(cells[_row+i][_col+j].getValue())){
        			candidat.remove(cells[_row+i][_col+j].getValue());
        		}
            	
            }
        }
		
    	}
    	return candidat;
	}

	@Override
	public void generateAllCandidat() {
		// TODO Auto-generated method stub
		for(int i=0;i<size;i++) {
            for(int j=0;j<size;j++) {
            	
            	cells[i][j].setCandidates(this.getPossibleCandidat(i,j));
            }
        }
	}

	  public Cell[] getCol(int col) {
	    	Cell cells[] = new Cell[size];
	    	for(int row=0;row<size;row++) {
	    	
	    		cells[row]=this.cells[row][col];
	    	}
	    	return cells;
	    }
	@Override
	public Cell[][] getGrid() {
		// TODO Auto-generated method stub
		return cells;
	}

	private boolean checkCell(int i,int j,String c){
		int count=0;
		for(Cell cell: getRegion(i, j) ) {
			if(cell.getValue()==c) {
				count++;
			}
		}
		for(Cell cell: getColumn(j) ) {
			if(cell.getValue()==c) {
				count++;
			}
		}
		for(Cell cell: cells[i] ) {
			if(cell.getValue()==c) {
				count++;
				}
			}
		if(count!=3) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkSolution() {
		// TODO Auto-generated method stub
		Set<String> CANDIDATES;
		
		CANDIDATES = defaultValueSet();
		for(int i=1;i<=size;i++) {
			CANDIDATES.add(Integer.toString(i));
		}
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				for(String c:CANDIDATES) {
						if(!checkCell(i, j, c)) return false;
				    }
				
				}	
			}
		return true;
		}


	@Override
	public void addPropertyChangeListener(String pName, PropertyChangeListener listener) {
		if(listener != null) {
			for(int i=0;i<size;i++) {
	            for(int j=0;j<size;j++) {
	            	
	            	cells[i][j].addPropertyChangeListener(pName, listener);
	            }
	        }
		}
	}
	
	
	/******* 
	 * 
	 * deleting candidates from region, line, and column
	 * 
	 * ******/
	
	
		
	}


	


	




	
