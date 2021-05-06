package heuristics;



import java.util.Set;
import java.util.TreeSet;

import cmd.Move;
import model.Cell;
import model.Coordinate;
import model.Grid;


public class UniqueCandidate  implements IHeuristic {

	// ATTRIBUTS
	

	private Grid grid;
	
	// CONSTRUCTEUR
	
	public UniqueCandidate(Grid grid) {
		this.grid=grid;
	

	}
	
	@Override
	public Move getSolution() {
		Move move = new Move();
		
		Integer[] nbPresentLine = new Integer[Grid.size];
		Integer[] nbPresentColumn = new Integer[Grid.size];
		Integer[] nbPresentRegion = new Integer[Grid.size];
		Object[][] candidates = new Object[Grid.size][Grid.size];
		Cell[] lastLine = new Cell[Grid.size];
		Cell[] lastColumn = new Cell[Grid.size];
		for (int i = 0; i < Grid.size; ++i) {
			nbPresentLine[i] = 0;
			nbPresentColumn[i] = 0;
			nbPresentRegion[i] = 0;
		}
		for (int i = 0; i < Grid.size; ++i) {
			for (int j = 0; j < Grid.size; ++j) {
				if (candidates[i][j] == null) {
					if (grid.getCellAt(i, j).getCandidates() == null) {
						candidates[i][j] = new TreeSet<String>();
					} else {
						candidates[i][j] = grid.getCellAt(i, j).getCandidates();
					}
				}
				if (candidates[j][i] == null) {
					if (grid.getCellAt(j, i).getCandidates() == null) {
						candidates[j][i] = new TreeSet<String>();
					} else {
						candidates[j][i] = grid.getCellAt(j, i).getCandidates();
					}
				}
			
				/*
				 * conter combien le candidat 
				 * est présent dans chaque ligne 
				 * 
				 */
				for (String s : (Set<String>) candidates[i][j]) {
					nbPresentLine[Integer.valueOf(s)-1] += 1;
					if (nbPresentLine[Integer.valueOf(s)-1] < 2) {
						lastLine[Integer.valueOf(s)-1] = grid.getCellAt(i, j);
					}
				}
				/*
				 * conter combien le candidat 
				 * est présent dans chaque colonne 
				 * 
				 */
				for (String s : (Set<String>) candidates[j][i]) {
					nbPresentColumn[Integer.valueOf(s)-1] += 1;
					if (nbPresentColumn[Integer.valueOf(s)-1] < 2) {
						lastColumn[Integer.valueOf(s)-1] = grid.getCellAt(j, i);
					}
				}
			}
			Coordinate cord = null;
			for (int j = 0; j < Grid.size; ++j) {
				if (nbPresentLine[j] == 1) {
					if(!UniqueValueAtUnity(lastLine[j],Integer.toString(j+1) )) {
						move.addAction(Move.SET_VALUE,lastLine[j], Integer.toString(j+1));
						
						move.setDetails(setDetails(lastLine[j], Integer.toString(j+1)));
						
						return move;
					}
				} else {
					nbPresentLine[j] = 0;
					if (nbPresentColumn[j] == 1) {
						if(!UniqueValueAtUnity(lastColumn[j], Integer.toString(j+1))) {
							
							move.addAction(Move.SET_VALUE,lastColumn[j], Integer.toString(j+1));
							
							move.setDetails(setDetails(lastColumn[j], Integer.toString(j+1)));
							return move;
						}
					} else {
						nbPresentColumn[j] = 0;
					}
				}
			}
		}
		return null;
	}
	private String setDetails(Cell cell,String j) {
		StringBuilder sb = new StringBuilder();
		sb.append("Candidat Unique:\n");
		sb.append("Le candidat '" + j + "' sur la ");
		sb.append( "colonne");
		sb.append(" est disponible uniquement sur la cellule de coordonnée ");
		sb.append("(" + (cell.getCoordinate().getY() + 1) + ";" + (cell.getCoordinate().getX() + 1) + ")");
		return sb.toString();
	}
	/**
	 * 
	 * @param cell 
	 * @param value
	 * parcours les unités (ligne, colonne, region) 
	 * si un candidat est uni
	 * @return 
	 */
	
	private boolean UniqueValueAtUnity(Cell cell, String value) {
		
		
		Coordinate bc = cell.getCoordinate();
		for (int i = 0; i < Grid.size; ++i) {
			if (i != bc.getX()) {
				if (value.equals(grid.getCellAt(i, bc.getY()).getValue())) {
					return true;
				}
			}
			if (i != bc.getY()) {
				if (value.equals(grid.getCellAt(bc.getX(), i).getValue())) {
					return true;
				}
			}
		}
		int _row=(bc.getX()/3)*3;
		int _col=(bc.getY()/3)*3;
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
					if(value.equals(grid.getCellAt(_row+i, _col+j).getValue())){
						return true;
					}
					
				}
			}
		
		return false;
	}
	
	
	
	
	


	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}


}
