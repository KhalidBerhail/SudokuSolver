package heuristics;

import java.awt.Color;
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
 * Impl�mentation de l'heuristique Swordfish
 * @author Nas
 *
 */
public class Swordfish  implements IHeuristic {

	// CONSTANTES
	
	public static final int MIN_NB_CAND = 2;
	public static final int MAX_NB_CAND = 3;
	
	public static final int MIN_NB_CELLS = 2;
	public static final int MAX_NB_CELLS = 3;
	
	public static final Color REASON_COLOR = Color.BLUE;
	public static final Color DELETE_COLOR = Color.ORANGE;
	
	
	// ATTRIBUTS
	
	private Map<Unity, Map<Integer, Cell[]>> data;
	private Grid grid;
	// CONSTRUCTEURS
	
	public Swordfish(Grid grid) {
		this.grid =grid;
		data = new HashMap<Unity, Map<Integer, Cell[]>>();
	}
	
	// REQUETES

	@Override
	public Move getSolution() {

		for (String cand : StdGrid.defaultValueSet()) {
			fillData(cand);
			if (!data.isEmpty()) {
				// 3 lignes / colonnes ==> 3 colonnes / lignes
				for (Unity u : Unity.values()) {
					if (!u.equals(Unity.REGION)) {
						int[] res = tryGetSolution1(u, cand);
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
								return setSolution1(u, cand, res);
							}
							correct = false;
						}
					}
				}
				// 3 lignes / colonnes ==> 2 colonnes / lignes et 1 r�gion
				
				// 2 lignes / colonnes et 1 r�gion ==> 2 colonnes / lignes et 2 r�gions
				
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
				if (cells != null && cells.length > 0) {
					fillData(u, i, cells);
				}
			}
		}
	}
	
	// --- Cas 2 --------------------------------------------------------------
	
	
	
	// --- Cas 1 --------------------------------------------------------------
	
	private Move setSolution1(Unity unit, String cand, int[] unities) {
		Move move = new Move();
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
					
							move.addAction(Move.ELIMINATE_CANDIDAT,ci, cand);
							move.addReason(  ci);
						
					}
				}
			}
		}
		move.setDetails(setDescription1(unit, cand));
	
		return move;
	}
	
	private String setDescription1(Unity unit, String cand) {
		StringBuilder res = new StringBuilder();
		res.append("\n Swordfish :\n");
		res.append("Swordfish avec les " + cand + ". Il est possible de "
				+ "supprimer les candidats " + cand + " des cases en rouge");
		
		return res.toString();
	}
	

	/**
	 * Renvoie l'unit� oppos�e (ligne -> colonne et colonne -> ligne)
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
	 * Test si les l'unit� oppos� � unit � des candidats � supprimer
	 * 
	 */
	private boolean isUseful1(Unity unit, String cand, int[] unities, 
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
	 * Test sur les cases des unit�s unit de num�ro unities ne poss�de que
	 * MAX_NB_CELLS colonnes en communs et si il y a des candidats � supprimer
	 * dans ces colonnes.
	 */
	private boolean testUnities1(Unity unit, String cand, int[] unities) {
		for (int unity : unities) {
			if (data.get(unit).get(unity) == null) {
				return false;
			}
		}
		Set<Integer> res = getOppositeUnities(unit, unities);
		if (res.size() == MAX_NB_CELLS) {
			if (isUseful1(unit, cand, unities, res)) {
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
	
//	private int[] tryGetSolution1(Unity unit, String cand) {
//		for (int i = 0; i < data.get(unit).size(); ++i) {
//			for (int j = i + 1; j < data.get(unit).size(); ++j) {
//				for (int k = j + 1; k < data.get(unit).size(); ++k) {
//					int[] tab = {(int) data.get(unit).keySet().toArray()[i],
//							(int) data.get(unit).keySet().toArray()[j],
//							(int) data.get(unit).keySet().toArray()[k]};
//					if (testUnities1(unit, cand, tab)) {
//						return tab;
//					}
//				}
//			}
//		}
//		return null;
//	}

	private int[] tryGetSolution1(Unity unit, String cand) {
		if (unit.equals(Unity.REGION) || data.get(unit) == null) {
			return null;
		}
		final int UNITY_NB = MAX_NB_CELLS;
		int[] res = new int[UNITY_NB];
		return tryGetSolution1(unit, cand, res, 0, 0, UNITY_NB);
	}
	
	private int[] tryGetSolution1(Unity unit, String cand, int[] tab, int nb, int index, int max) {
		if (nb >= max) {
			if (testUnities1(unit, cand, tab)) {
				return tab;
			}
			return null;
		} else {
			for (int i = index; i < data.get(unit).size(); ++i) {
				tab[nb] = (int) data.get(unit).keySet().toArray()[i];
				int[] res = tryGetSolution1(unit, cand, tab, nb + 1, i + 1, max);
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}
	
	/**
	 * Retourne le tableau des cellules  de l'unit� u avec le candidat cand 
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
	
	// --- Cas 3 --------------------------------------------------------------
	
	// --- Auxiliaire ---------------------------------------------------------
	
	/**
	 * Remplie la variable data avec l'unit� unit, le num�ro de l'unit� nb
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
	 * Permet de r�cup�rer la coordonn�e x d'une case en fonction de l'unit� u,
	 *  le num�ro de l'unit� n et le num�ro de la case dans l'unit�.
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
	 * Permet de r�cup�rer la coordonn�e y d'une case en fonction de l'unit� u,
	 *  le num�ro de l'unit� n et le num�ro de la case dans l'unit�.
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
