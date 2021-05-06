package heuristics;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cmd.Move;
import model.Coordinate;
import model.Cell;
import model.Grid;

public class XyWing  implements IHeuristic {
	private Grid grid;
	public XyWing(Grid grid) {
		this.grid = grid;
	}

	@Override
	public Move getSolution() {
	
		
		for (Cell cell : grid.getCells()) {
			
			// Pour toutes les cellules contenant uniquement 2 candidats
			if (cell.getCandidates() != null && cell.getCandidates().size() == 2) {
				Coordinate bc = cell.getCoordinate();
				Map<Cell, String> mapFirst = new HashMap<Cell, String>();
				Map<Cell, String> mapSecond = new HashMap<Cell, String>();
				
				
				boolean contain = false;
				
				// On vérifie sur la ligne si elle contient des cases à 2 candidats
				for (int k = 0; k < Grid.size; ++k) {
					if (k != bc.getX()) {
						Cell firstCell = grid.getCellAt(bc.getX(), k);
						
						// Pour chaque cellule cellules sur la même ligne contenant uniquement
						//	2 candidats
						if (firstCell.getCandidates() != null &&
								firstCell.getCandidates().size() == 2) {
							
							// On stocke la cellule avec la valeur différente
							String value = getDifferenceOnlyOne(cell.getCandidates(), firstCell.getCandidates());
							if (value != null) {
								contain = true;
								mapFirst.put(firstCell, value);
							}
						}
					}
				}
				
				// Si la ligne contient au moins une case à 2 candidats
				if (contain) {
					contain = false;
					
					// On vérifie sur la colonne si elle contient des cases à 2 candidats
					for (int k = 0; k < Grid.size; ++k) {
						if (k != bc.getY()) {
							Cell SecondCell = grid.getCellAt(k, bc.getY());
							
							// Pour chaque cellule cellules sur la même colonne contenant uniquement
							//	2 candidats
							if (SecondCell.getCandidates() != null &&
									SecondCell.getCandidates().size() == 2) {
								
								// On stocke la cellule avec la valeur différente
								String value = getDifferenceOnlyOne(cell.getCandidates(), SecondCell.getCandidates());
								if (value != null) {
									contain = true;
									mapSecond.put(SecondCell, value);
								}
							}
						}
					}
				}
				
				// Si la ligne ET la colonne contiennent au moins une case à 2 candidats
				if (contain) {
					for (Cell cellFirst : mapFirst.keySet()) {
						String valueFirst = mapFirst.get(cellFirst);
						
						for (Cell cellSecond : mapSecond.keySet()) {
							String valueSecond = mapSecond.get(cellSecond);
							
							if (valueFirst.equals(valueSecond)) {
								Cell remove = grid.getCellAt(
										cellSecond.getCoordinate().getX(),
										cellFirst.getCoordinate().getY());
								if (remove.getCandidates() != null &&
										remove.getCandidates().contains(valueFirst)) {
									Move move = new Move();
									setSolution( move, cell, cellFirst, cellSecond, remove, valueFirst);
									return move;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private void setSolution(Move move, Cell beginning, Cell first, Cell second, Cell remove, String value) {
	
			move.addReason(beginning);
			move.addReason(first);
			move.addReason(second);
			move.addReason(remove);
			move.addAction(Move.ELIMINATE_CANDIDAT,remove, value);
		StringBuilder sb = getDescription(beginning, first, second, remove, value);
		move.setDetails(sb.toString());
	}
	
	private StringBuilder getDescription(Cell beginning, Cell first, Cell second, Cell remove, String value) {
		String v1 = null;
		String v2 = null;
		for (String v : beginning.getCandidates()) {
			if (v1 == null) {
				v1 = v;
			} else {
				v2 = v;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("\n XYWING :\n");
		sb.append("Quel que puisse être le choix dans la case de départ de coordonnée " + beginning.getCoordinate());
		sb.append(", on est certain de ne pas retrouver le candidat ");
		sb.append(value + " dans la case finale case en couleur.\n");//to do
		sb.append("Si on choisit " + v1 + ", c'est " + value + " qui va prendre place ");
		if (getCommunValue(beginning, first) == v1) {
			sb.append("dans la colonne.");
		} else {
			sb.append("dans la ligne.");
		}
		sb.append(" Et si on choisit le " + v2 + ", le " + value + " prendra la place ");
		if (getCommunValue(beginning, second) == v1) {
			sb.append("dans la colonne.");
		} else {
			sb.append("dans la ligne.");
		}
		sb.append("Dans les deux cas, la case en orange ne peut pas être " + value + ".");
		sb.append("On peut donc supprimer " + value + " des candidats pour cette case.");
		return sb;
	}
	
	private String getDifferenceOnlyOne(Set<String> a, Set<String> b) {
		String value = null;
		for (String currentValue : b) {
			if (value == null) {
				value = (!a.contains(currentValue) ? currentValue : null);
			} else {
				if (!a.contains(currentValue)) {
					return null;
				}
			}
		}
		return value;
	}
	
	private String getCommunValue(Cell cell1, Cell cell2) {
		String value = null;
		boolean isGood = false;
		for (String candidateLine : cell2.getCandidates()) {
			if (cell1.getCandidates().contains(candidateLine)) {
				if (isGood) {
					return null;
				}
				isGood = true;
				value = candidateLine;
			}
		}
		return value;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}


}

