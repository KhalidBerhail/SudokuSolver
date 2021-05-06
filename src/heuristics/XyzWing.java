package heuristics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cmd.Move;
import model.Cell;
import model.Coordinate;
import model.Grid;
import model.StdGrid;

public class XyzWing  implements IHeuristic {

	private Map<String, Integer> mapStrings;
	private Map<Integer, String> mapValues;
	private Grid grid;
	public XyzWing(Grid grid) {
		this.grid  = grid;
		mapStrings = new HashMap<String, Integer>();
		mapValues = new HashMap<Integer, String>();
		setMapStrings(StdGrid.defaultValueSet());
		setMapValues(StdGrid.defaultValueSet());
	}

	@Override
	public Move getSolution() {
		
		
		
		
		for (Cell cell : grid.getCells()) {

			
			// Pour toutes les cellules contenant uniquement 3 candidats
			if (cell.getCandidates() != null && cell.getCandidates().size() == 3) {
				Coordinate bc = cell.getCoordinate();
				Map<Cell, String> mapFirst = new HashMap<Cell, String>();
				Map<Cell, String> mapSecond = new HashMap<Cell, String>();
				
				
				boolean contain = false;
				
				// On vérifie sur la ligne si elle contient des cases à 2 candidats
				for (int k = 0; k < Grid.size; ++k) {
					if (k != bc.getX()) {
						Cell firstCell = grid.getCellAt(bc.getX(), k);
						
						// Pour chaque cellule sur la même ligne contenant uniquement
						//	2 candidats
						if (firstCell.getCandidates() != null &&
								firstCell.getCandidates().size() == 2) {
							
							// On stocke la cellule avec la valeur différente
							String value = getDifferenceOnlyOne(firstCell.getCandidates(), cell.getCandidates());
							if (value != null) {
								contain = true;
								mapFirst.put(firstCell, value);
							}
						}
					}
				}
				
				// Si la ligne contient au moins une case à 2 candidats
				if (contain) {
					int startX = bc.getX() - bc.getX() % Grid.REGION_SIZE;
					int startY = bc.getY() - bc.getY() % Grid.REGION_SIZE;
					for (int i = 0; i < Grid.REGION_SIZE ; ++i) {
						for (int j = 0; j < Grid.REGION_SIZE; ++j) {
							
							
							if ((i + startX) != bc.getX() && (j + startY) != bc.getY()) {
								Cell secondCell = grid.getCellAt(i + startX, j + startY);
								
								// Pour chaque cellule dans la même région que cell ayant 
								//	uniquement 2 candidats
								if (secondCell.getCandidates() != null &&
										secondCell.getCandidates().size() == 2) {
									
									// On stocke la cellule avec la valeur différente
									String value = getDifferenceOnlyOne(secondCell.getCandidates(), cell.getCandidates());
									if (value != null) {
										contain = true;
										mapSecond.put(secondCell, value);
									}
								}
							}
						}
					}
				}
				
				// Si la ligne ET la région contiennent au moins une case à 2 candidats
				if (contain) {
					for (Cell cellFirst : mapFirst.keySet()) {
						String valueFirst = mapFirst.get(cellFirst);
						
						for (Cell cellSecond : mapSecond.keySet()) {
							String valueSecond = mapSecond.get(cellSecond);
							
							// Si la valeur différentes sont différentes
							if (!valueFirst.equals(valueSecond)) {
								String valueToRemove = getFirstNotContain(cell.getCandidates(), valueFirst, valueSecond);
								List<Cell> removes = new ArrayList<Cell>();
								boolean fromLine = false;
								
								// Récupère les infos sur si on doit éliminer sur la ligne ou la colonne
								if (fromSameRegion(cell, cellFirst)) {
									if (fromSameLine(cell, cellSecond)) {
										fromLine = true;
									}
								} else {
									if (fromSameLine(cell, cellFirst)) {
										fromLine = true;
									}
								}
								
								// Récupère les cellules à retirer le candidat
								if (fromLine) {
									for (int k = 0; k < Grid.REGION_SIZE; ++k) {
										if ((bc.getY() - bc.getY() % Grid.REGION_SIZE) + k != bc.getY()) {
											Cell cellRemove = grid.getCellAt(
													bc.getX(),
													(bc.getY() - bc.getY() % Grid.REGION_SIZE) + k);
											if (cellRemove.getCandidates() != null &&
													cellRemove.getCandidates().contains(valueToRemove)) {
												removes.add(cellRemove);
											}
										}
									}
								} else {
									for (int k = 0; k < Grid.REGION_SIZE; ++k) {
										if ((bc.getX() - bc.getX() % Grid.REGION_SIZE) + k != bc.getX()) {
											Cell cellRemove = grid.getCellAt(
													(bc.getX() - bc.getX() % Grid.REGION_SIZE) + k,
													bc.getY());
											if (cellRemove.getCandidates() != null &&
													cellRemove.getCandidates().contains(valueToRemove)) {
												removes.add(cellRemove);
											}
										}
									}
								}
								
								// Si il y a des cellules avec le candidat à retirer
								if (removes.size() > 0) {
									Move move =new  Move();
									setSolution(move, cell, cellFirst, cellSecond, removes, valueToRemove, fromLine);
									return move;
								}
								
							}
						}
					}
				}
				
				mapFirst.clear();
				mapSecond.clear();
				contain = false;
				
				// On vérifie sur la ligne si elle contient des cases à 2 candidats
				for (int k = 0; k < Grid.size; ++k) {
					if (k != bc.getX()) {
						Cell firstCell = grid.getCellAt(k, bc.getY());
						
						// Pour chaque cellule sur la même ligne contenant uniquement
						//	2 candidats
						if (firstCell.getCandidates() != null &&
								firstCell.getCandidates().size() == 2) {
							
							// On stocke la cellule avec la valeur différente
							String value = getDifferenceOnlyOne(firstCell.getCandidates(), cell.getCandidates());
							if (value != null) {
								contain = true;
								mapFirst.put(firstCell, value);
							}
						}
					}
				}
				
				// Si la ligne contient au moins une case à 2 candidats
				if (contain) {
					int startX = bc.getX() - bc.getX() % Grid.REGION_SIZE;
					int startY = bc.getY() - bc.getY() % Grid.REGION_SIZE;
					for (int i = 0; i < Grid.REGION_SIZE ; ++i) {
						for (int j = 0; j < Grid.REGION_SIZE; ++j) {
							
							
							if ((i + startX) != bc.getX() && (j + startY) != bc.getY()) {
								Cell secondCell = grid.getCellAt(i + startX, j + startY);
								
								// Pour chaque cellule dans la même région que cell ayant 
								//	uniquement 2 candidats
								if (secondCell.getCandidates() != null &&
										secondCell.getCandidates().size() == 2) {
									
									// On stocke la cellule avec la valeur différente
									String value = getDifferenceOnlyOne(secondCell.getCandidates(), cell.getCandidates());
									if (value != null) {
										contain = true;
										mapSecond.put(secondCell, value);
									}
								}
							}
						}
					}
				}
				
				// Si la ligne ET la région contiennent au moins une case à 2 candidats
				if (contain) {
					for (Cell cellFirst : mapFirst.keySet()) {
						String valueFirst = mapFirst.get(cellFirst);
						
						for (Cell cellSecond : mapSecond.keySet()) {
							String valueSecond = mapSecond.get(cellSecond);
							
							// Si la valeur différentes sont différentes
							if (!valueFirst.equals(valueSecond)) {
								String valueToRemove = getFirstNotContain(cell.getCandidates(), valueFirst, valueSecond);
								List<Cell> removes = new ArrayList<Cell>();
								boolean fromLine = false;
								
								// Récupère les infos sur si on doit éliminer sur la ligne ou la colonne
								if (fromSameRegion(cell, cellFirst)) {
									if (fromSameLine(cell, cellSecond)) {
										fromLine = true;
									}
								} else {
									if (fromSameLine(cell, cellFirst)) {
										fromLine = true;
									}
								}
								
								// Récupère les cellules à retirer le candidat
								if (fromLine) {
									for (int k = 0; k < Grid.REGION_SIZE; ++k) {
										if ((bc.getY() - bc.getY() % Grid.REGION_SIZE) + k != bc.getY()) {
											Cell cellRemove = grid.getCellAt(
													bc.getX(),
													(bc.getY() - bc.getY() % Grid.REGION_SIZE) + k);
											if (cellRemove.getCandidates() != null &&
													cellRemove.getCandidates().contains(valueToRemove)) {
												removes.add(cellRemove);
											}
										}
									}
								} else {
									for (int k = 0; k < Grid.REGION_SIZE; ++k) {
										if ((bc.getX() - bc.getX() % Grid.REGION_SIZE) + k != bc.getX()) {
											Cell cellRemove = grid.getCellAt(
													(bc.getX() - bc.getX() % Grid.REGION_SIZE) + k,
													bc.getY());
											if (cellRemove.getCandidates() != null &&
													cellRemove.getCandidates().contains(valueToRemove)) {
												removes.add(cellRemove);
											}
										}
									}
								}
								
								// Si il y a des cellules avec le candidat à retirer
								if (removes.size() > 0) {
									Move move =new  Move();
									setSolution(move, cell, cellFirst, cellSecond, removes, valueToRemove, fromLine);
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
	
	
	
	private void setSolution(Move move, Cell beginning, Cell first, Cell second, List<Cell> removes,
		String valueToRemove, boolean fromLine) {
		move.addReason( beginning);
		move.addReason( first);
		move.addReason( second);
		
			for (Cell cell : removes) {
				move.addReason( cell);
				move.addAction(Move.ELIMINATE_CANDIDAT, cell, valueToRemove);
			}
		
		StringBuilder sb = getDescription(beginning, first, second, removes, valueToRemove, fromLine);
		move.setDetails(sb.toString());
		
	}

	private StringBuilder getDescription(Cell beginning, Cell first, Cell second, List<Cell> removes, String valueToRemove, boolean fromLine) {
		String v1 = null;
		String v2 = null;
		
		for (String v : beginning.getCandidates()) {
			if (v1 == null) {
				v1 = v;
			} else if (v2 == null) {
				v2 = v;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append(" \n XYZWING :\n");
		sb.append("Quel que puisse être le choix dans la case de départ de coordonnée " + beginning.getCoordinate());
		sb.append(", on est certain de ne pas retrouver le candidat ");
		sb.append(valueToRemove + " dans les case finale en couleur.");//to do
		sb.append("Si on choisit " + v1 + ", c'est " + valueToRemove + " qui va prendre place ");
		if (getCommunValue(beginning, first, valueToRemove) == v1) {
			if (fromLine) {
				sb.append("sur la colonne.");
			} else {
				sb.append("sur la ligne.");
			}
		} else {
			sb.append("dans celui dans le même carré.");
		}
		sb.append(" Et si on choisit le " + v2 + ", le " + valueToRemove + " prendra la place ");
		if (getCommunValue(beginning, second, valueToRemove) == v2) {
			sb.append("dans celui dans le même carré.");
		} else {
			if (fromLine) {
				sb.append("sur la colonne.");
			} else {
				sb.append("sur la ligne.");
			}
		}
		sb.append(" Dans les deux cas, les cases en magenta ne peuvent pas être " + valueToRemove + ".");
		sb.append("On peut donc supprimer " + valueToRemove + " des candidats pour ces cases.");
		return sb;
	}

	private String getFirstNotContain(Set<String> list, String a, String b) {
		for (String s : list) {
			if (s != a && s != b) {
				return s;
			}
		}
		return null;
	}
	
	private boolean fromSameRegion(Cell first, Cell second) {
		Coordinate bc1 = first.getCoordinate();
		Coordinate bc2 = second.getCoordinate();
		
		int startX1 = bc1.getX() - bc1.getX() % Grid.REGION_SIZE;
		int startY1 = bc1.getY() - bc1.getY() % Grid.REGION_SIZE;
		int startX2 = bc2.getX() - bc2.getX() % Grid.REGION_SIZE;
		int startY2 = bc2.getY() - bc2.getY() % Grid.REGION_SIZE;
		return (startX1 == startX2 && startY1 == startY2);
	}
	
	private boolean fromSameLine(Cell first, Cell second) {
		Coordinate bc1 = first.getCoordinate();
		Coordinate bc2 = second.getCoordinate();
		return (bc1.getX() == bc2.getX());
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
	
	

	private String getCommunValue(Cell cell1, Cell cell2, String notThisValue) {
		String value = null;
		boolean isGood = false;
		for (String candidate : cell1.getCandidates()) {
			if (candidate != notThisValue && cell2.getCandidates().contains(candidate)) {
				if (isGood) {
					return null;
				}
				isGood = true;
				value = candidate;
			}
		}
		return value;
	}
	
	private void setMapStrings(Collection<String> collection) {
		int i = 0;
		for (String s : collection) {
			mapStrings.put(s, i);
			++i;
		}
	}
	
	private void setMapValues(Collection<String> collection) {
		int i = 0;
		for (String s : collection) {
			mapValues.put(i, s);
			++i;
		}
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}


}
