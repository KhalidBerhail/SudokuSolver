package heuristics;



import java.util.Collection;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cmd.Move;
import model.Grid;
import model.StdGrid;


public class Xwing  implements IHeuristic {

	private Map<String, Integer> mapStrings;
	private Map<Integer, String> mapValues;
	private Grid  grid ; 
	private static final int NB_ELEMENT = 2;
	
	public Xwing(Grid grid) {
		this.grid = grid;
		mapStrings = new HashMap<String, Integer>();
		mapValues = new HashMap<Integer, String>();
		setMapStrings(StdGrid.defaultValueSet());
		setMapValues(StdGrid.defaultValueSet());
	}

	@Override
	public Move getSolution() {
		Move sol = null;
		if ((sol = checkFirstSolution()) != null && sol.getActions().size() > 0) {
			return sol;
		}/*
		if ((sol = checkSecondSolution()) != null && sol.getActions().size() > 0) {
			return sol;
		}*/
		return null;
	}
	

	

	private Move checkFirstSolution() {
	
		
		Set<Integer> setFirstLine = new TreeSet<Integer>();
		Set<Integer> setFirstColumn = new TreeSet<Integer>();
		Set<Integer> setSecondLine = new TreeSet<Integer>();
		Set<Integer> setSecondColumn = new TreeSet<Integer>();
		for (String value : mapValues.values()) {
			int indexFirst = 0;
			int indexSecond = 0;
			boolean notValide = false;
			while (indexFirst < Grid.size - 1) {
				int k = 0;
				while (k < Grid.size && 
						(setFirstLine.size() <= NB_ELEMENT || setFirstColumn.size() <= NB_ELEMENT)) {
					if (grid.getCellAt(indexFirst, k).getCandidates() != null &&
							grid.getCellAt(indexFirst, k).getCandidates().contains(value)) {
						setFirstLine.add(k);
					}
					if (grid.getCellAt(k, indexFirst).getCandidates() != null &&
							grid.getCellAt(k, indexFirst).getCandidates().contains(value)) {
						setFirstColumn.add(k);
					}
					++k;
				}
				indexSecond = indexFirst + 1;
				notValide = false;
				if (setFirstLine.size() == NB_ELEMENT) {
					while (indexSecond < Grid.size) {
						k = 0;
						while (k < Grid.size && setSecondLine.size() <= NB_ELEMENT) {
							if (!notValide && 
									grid.getCellAt(indexSecond, k).getCandidates() != null &&
									grid.getCellAt(indexSecond, k).getCandidates().contains(value)) {
								notValide = !setFirstLine.contains(k);
								setSecondLine.add(k);
							}
							++k;
						}
						if (!notValide && setFirstLine.equals(setSecondLine)) {
							Move move = new Move();
							setFirstSolution((Move) move,  value, indexFirst, indexSecond,
									setSecondLine, true);
							if (move.getActions().size() > 0) {
								return move;
							}
						}
						setSecondLine.clear();
						notValide = false;
						++indexSecond;
					}
				}
				indexSecond = indexFirst + 1;
				notValide = false;
				if (setFirstColumn.size() == NB_ELEMENT) {
					while (indexSecond < Grid.size) {
						k = 0;
						while (k < Grid.size && setSecondColumn.size() <= NB_ELEMENT) {
							if (!notValide && 
									grid.getCellAt(k, indexSecond).getCandidates() != null &&
									grid.getCellAt(k, indexSecond).getCandidates().contains(value)) {
								notValide = !setFirstColumn.contains(k);
								setSecondColumn.add(k);
							}
							++k;
						}
						if (!notValide && setFirstColumn.equals(setSecondColumn)) {
							Move move = new Move();
							setFirstSolution(move,  value, indexFirst, indexSecond,
									setSecondColumn, false);
							if (move.getActions().size() > 0) {
								return move;
							}
						}
						setSecondColumn.clear();
						notValide = false;
						++indexSecond;
					}
				}
				setFirstLine.clear();
				setFirstColumn.clear();
				++indexFirst;
			}
		}
		return null;
	}
	
	private void setFirstSolution(Move move, String value, int num1, int num2,
			Set<Integer> list, boolean fromLine) {
	
		for (int i = 0; i < Grid.size; ++i) {
			if (i != num1 && i != num2) {
				for (Integer j : list) {
					if (fromLine && grid.getCellAt(i, j).getCandidates() != null &&
							grid.getCellAt(i, j).getCandidates().contains(value)) {
						
							
						move.addAction(Move.ELIMINATE_CANDIDAT,grid.getCellAt(i, j),value);
							move.addReason( grid.getCellAt(i, j));
					
					} else if (!fromLine && grid.getCellAt(j, i).getCandidates() != null &&
							grid.getCellAt(j, i).getCandidates().contains(value)){
						
						move.addAction(Move.ELIMINATE_CANDIDAT,grid.getCellAt(j, i),value);
							move.addReason( grid.getCellAt(j, i));
						
					}
				}
			} else {
				for (Integer j : list) {
					move.addReason( grid.getCellAt(i, j));
				}
			}
		}
		StringBuilder description = getFirstDescription(value, num1, num2, list, fromLine);
		move.setDetails(description.toString());
	}
	
	private StringBuilder getFirstDescription(String value, int num1, int num2, Set<Integer> list,
			boolean fromLine) {
		StringBuilder sb = new StringBuilder();
		sb.append(" XWING :\n");
		sb.append("Le candidat '" + value + "'");
		sb.append(" n'est disponible qu'à " + list.size() + " emplacement sur les ");
		sb.append(fromLine ? "lignes" : "colonnes");
		sb.append(" numéro " + num1 + " et " + num2);
		sb.append(".\nDe plus, ces candidats font partie des ");
		sb.append(fromLine ? "colonnes" : "lignes");
		Object[] values = list.toArray();
		sb.append(" " + values[0]);
		for (int i = 1; i < list.size(); ++i) {
			sb.append(" et " + values[i]);
		}
		sb.append(".\nIl est donc possible de supprimer tous les autres" +
				" candidats '" + value + "' de ces " + list.size() + " ");
		sb.append(fromLine ? "colonnes." : "lignes.");
		return sb;
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
