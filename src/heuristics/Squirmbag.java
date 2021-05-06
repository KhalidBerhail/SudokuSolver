package heuristics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cmd.Move;
import helpers.Unity;
import model.Cell;
import model.Grid;
import model.StdGrid;

/**
 * Implémentation de l'heuristique Squirmbag
 * @author Nas
 *
 */
public class Squirmbag  implements IHeuristic {

	// CONSTANTES
	public static final int MIN_NB_CAND = 2;
	public static final int MAX_NB_CAND = 5;
	
	public static final int MIN_NB_CELLS = 2;
	public static final int MAX_NB_CELLS = 5;
	

	
	
	// ATTRIBUTS
	
	private Map<Unity, Map<Integer, Cell[]>> data;
	private Grid grid;
	// CONSTRUCTEURS
	
	public Squirmbag(Grid grid) {
		this.grid = grid;
		data = new HashMap<Unity, Map<Integer, Cell[]>>();
	}
	
	// REQUETES

	@Override
	public Move getSolution() {

		for (String cand : StdGrid.defaultValueSet()) {
			fillData(cand);
			if (!data.isEmpty()) {
				for (Unity u : Unity.values()) {
					if (!u.equals(Unity.REGION)) {
						int[] res = tryGetSolution(u, cand);
						if (res != null) {
							boolean correct = true;
							Set<Integer> op = getOppositeUnities(u, res);
							for (Integer i : res) {
								for (int j = 0; j < Grid.size; ++j) {
									if (op.contains(j)) {
										Cell ci = u.equals(Unity.LINE) ? grid.getCellAt(i, j) : 
											grid.getCellAt(j, i);
										if (ci.getValue() == null && !ci.getCandidates().contains(cand)) {
											correct = false;
										}
									}
								}
							}
							if (correct) {
								return setSolution(u, cand, res);
							}
							correct = false;
						}
					}
				}
				data.clear();	
			}
		}
		return null;
	}
	
	// OUTILS

	/**
	 * Remplit la variable data pour le candidat cand
	 */
	private void fillData(String cand) {
		for (Unity u : Unity.values()) {
			for (int i = 0; i < Grid.size; ++i) {
				Cell[] cells = getValidCells(u, i, cand);
				if (cells != null) {
					fillData(u, i, cells);
				}
			}
		}
	}

	private Move setSolution(Unity unit, String cand, int[] unities) {
		Move move=new Move();
		Set<Integer> op = getOppositeUnities(unit, unities);
		for (Integer i : unities) {
			for (int j = 0; j < Grid.size; ++j) {
				if (op.contains(j)) {
					Cell ci = unit.equals(Unity.LINE) ? grid.getCellAt(i, j) : 
						grid.getCellAt(j, i);
					move.addReason( ci);
				}
			}
		}
		for (int i : op) {
			for (int j = 0; j < Grid.size; ++j) {
				if (!isInTab(unities, j)) {
					Cell ci = !unit.equals(Unity.LINE) 
							? grid.getCellAt(i, j) 
							: grid.getCellAt(j, i);
					if (ci.getCandidates() != null 
							&& ci.getCandidates().contains(cand)) {
						
							move.addAction(Move.ELIMINATE_CANDIDAT, ci, cand);
							move.addReason(  ci);
						
					}
				}
			}
		}
		
		move.setDetails(setDescription(unit, cand));
		
		return move;
	}
	
	private String setDescription(Unity unit, String cand) {
		StringBuilder res = new StringBuilder();
		res.append("\n Squirmbag:\n");
		res.append("Squirmbag avec les " + cand + ". Il est possible de "
				+ "supprimer les candidats " + cand + " des cases en rouge");
		return res.toString();
	}
	

	/**
	 * Renvoie l'unitï¿½ opposï¿½e (ligne -> colonne et colonne -> ligne)
	 */
	private Unity getOppositeUnity(Unity unit) {
		switch (unit) {
		case LINE:
			return Unity.COL;
		case COL: 
			return Unity.LINE;
		case REGION:
			return null;
		default:
			return null;
		}
	}
	
	/**
	 * Test si les l'unitï¿½ opposï¿½ ï¿½ unit ï¿½ des candidats ï¿½ supprimer
	 * 
	 */
	private boolean isUseful(Unity unit, String cand, int[] unities, 
			Set<Integer> opposite) {
		Unity op = getOppositeUnity(unit);
		if (op == null) {
			return false;
		}
		
		for (Integer i : opposite) {
			for (int j = 0; j < Grid.size; ++j) {
				int x = unit.equals(Unity.LINE) ? j : i;
				int y = unit.equals(Unity.LINE) ? i : j;
				Cell ci = grid.getCellAt(x, y);
				int coord = unit.equals(Unity.LINE) ? ci.getCoordinate().getX() 
						: ci.getCoordinate().getY();
				if (!isInTab(unities, coord) 
						&& ci.getCandidates() != null
						&& ci.getCandidates().contains(cand)) {
					return true;
				}
			}
		}
		return false;
	}
		

	/**
	 * Test sur les cases des unitï¿½s unit de numï¿½ro unities ne possï¿½de que
	 * MAX_NB_CELLS colonnes en communs et si il y a des candidats ï¿½ supprimer
	 * dans ces colonnes.
	 */
	private boolean testUnities(Unity unit, String cand, int[] unities) {
		for (int unity : unities) {
			if (data.get(unit).get(unity) == null) {
				return false;
			}
		}
		Set<Integer> res = getOppositeUnities(unit, unities);
		if (res.size() == MAX_NB_CELLS) {
			if (isUseful(unit, cand, unities, res)) {
				return true;
			}
		}
		return false;
	}
	
	private Set<Integer> getOppositeUnities(Unity unit, int[] unities) {
		Set<Integer> res = new HashSet<>();
		for (int i : unities) {
			if (data.get(unit).get(i) != null) {
				for (Cell ci : data.get(unit).get(i)) {
					res.add((unit.equals(Unity.LINE) ? ci.getCoordinate().getY() 
							: ci.getCoordinate().getX()));
				}	
			}
		}
		return res;
	}
	

	private int[] tryGetSolution(Unity unit, String cand) {
		if (unit.equals(Unity.REGION) || data.get(unit) == null) {
			return null;
		}
		final int UNITY_NB = MAX_NB_CELLS;
		int[] res = new int[UNITY_NB];
		return tryGetSolution(unit, cand, res, 0, 0, UNITY_NB);
	}
	
	private int[] tryGetSolution(Unity unit, String cand, int[] tab, int nb, int index, int max) {
		if (nb >= max) {
			if (testUnities(unit, cand, tab)) {
				return tab;
			}
			return null;
		} else {
			for (int i = index; i < data.get(unit).size(); ++i) {
				tab[nb] = (int) data.get(unit).keySet().toArray()[i];
				int[] res = tryGetSolution(unit, cand, tab, nb + 1, i + 1, max);
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}
	
	/**
	 * Retourne le tableau des cellules  de l'unitï¿½ u avec le candidat cand 
	 * si leur nombres est >= MIN_NB_CAND et <= MAX_NB_CAND
	 */
	private Cell[] getValidCells(Unity unit, int nb, String cand) {
		int cpt = 0;
		for (int i = 0; i < Grid.size; ++i) {
			int x = getX(unit, nb, i);
			int y = getY(unit, nb, i);
			if (grid.getCellAt(x, y).getCandidates() != null) {
				if (grid.getCellAt(x, y).getCandidates().contains(cand)) {
					cpt++;
				}
			}
		}
		if (cpt < MIN_NB_CAND || cpt > MAX_NB_CAND) {
			return null;
		}
		Cell[] res = new Cell[cpt];
		cpt = 0;
		for (int i = 0; i < Grid.size; ++i) {
			int x = getX(unit, nb, i);
			int y = getY(unit, nb, i);
			Cell ci = grid.getCellAt(x, y);
			if (ci.getCandidates() != null) {
				if (ci.getCandidates().contains(cand)) {
					res[cpt] = ci;
					cpt++;
				}
			}
		}
		return res;
	}

	/**
	 * Remplie la variable data avec l'unitï¿½ unit, le numï¿½ro de l'unitï¿½ nb
	 * et les cellules cells.
	 */
	private void fillData(Unity unit, int nb, Cell[] cells) {
		if (unit == null || nb < 0 || nb >= Grid.size
				|| cells == null) {
			return;
		}

		if (!data.containsKey(unit)) {
			data.put(unit, new HashMap<Integer, Cell[]>());
		}
		data.get(unit).put(nb, cells);
	}
	
	/**
	 * Permet de rï¿½cupï¿½rer la coordonnï¿½e x d'une case en fonction de l'unitï¿½ u,
	 *  le numï¿½ro de l'unitï¿½ n et le numï¿½ro de la case dans l'unitï¿½.
	 */
	private int getX(Unity u, int n, int nb) {
		
		int x  = -1;
		switch (u) {
			case LINE:
				x = n;
				break;
			case COL:
				x = nb;
				break;
			case REGION:
				x = (n / Grid.REGION_SIZE) * Grid.REGION_SIZE + nb / Grid.REGION_SIZE;
				break;
			default:
				break;
		}
		return x;
	}
	
	/**
	 * Permet de rï¿½cupï¿½rer la coordonnï¿½e y d'une case en fonction de l'unitï¿½ u,
	 *  le numï¿½ro de l'unitï¿½ n et le numï¿½ro de la case dans l'unitï¿½.
	 */
	private int getY(Unity u, int n, int nb) {
		int y  = -1;
		switch (u) {
			case LINE:
				y = nb;
				break;
			case COL:
				y = n;
				break;
			case REGION:
				y = (n % Grid.REGION_SIZE) * Grid.REGION_SIZE + nb % Grid.REGION_SIZE;
				break;
			default:
				break;
		}
		return y;
	}
	
	/**
	 * Test si n est dans l tableau tab
	 */
	private boolean isInTab(int[] tab, int n) {
		for (int i : tab) {
			if (n == i) {
				return true;
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
