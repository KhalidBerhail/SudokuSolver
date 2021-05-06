package heuristics;

import java.util.HashMap;
import java.util.Map;

import cmd.Move;
import helpers.Unity;

import java.awt.Color;

import model.Cell;
import model.Grid;
import model.StdGrid;

public class Coloring  implements IHeuristic {

	// CONSTANTES
	
	public static final int IDENTICALS_CELLS = 2;
	public static final int MINIMUM_CELLS = 4;
	

	// ATTRIBUTS
	
	/**
	 * Contient les cellules qui sont seules ï¿½ avoir le mï¿½me candidat sur une 
	 * unitï¿½.
	 */
	private Cell[] start;
	
	/**
	 * Contient les cellules rï¿½sultats
	 */
	private Map<Cell, Colour> result;
	private Grid grid;
	
	// CONSTRUCTEURS
	
	public Coloring(Grid grid) {
		this.grid = grid;
		start = new Cell[IDENTICALS_CELLS];
		result = new HashMap<Cell, Colour>();
	}
	
	// REQUETES
	
	@Override
	public Move getSolution() {
		for (Unity u : Unity.values()) {
			for (String cand : StdGrid.defaultValueSet()) {
				for (int i = 0; i < Grid.size; ++i) {
					onlyTwoCandidates(cand, u, i);
					if (testStart()) {
						fillResult(cand, u, i);	
						// Crï¿½ation du tableau de cellules contenues dans result.
						Cell[] cells = new Cell[result.size()];
						int cpt = 0;
						for (Cell ci : result.keySet()) {
							cells[cpt] = ci;
							++cpt;
						}
						Colour col = testResult(cells, cand);
						if (col != null) {
							return setSolution(col, cand);
						} else {
							Cell cell = testResult2(cells, cand);
							if (cell != null) {
								return setSolution(cell, cand);
							} else {
								Cell[] cls = testResult3(cells, cand);
								if (cls != null) {
									return setSolution(cls, cand);
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	// OUTILS
	
	/**
	 * Crï¿½er la solution une fois que l'heuristique est trouvï¿½e.
	 * Coloriage est une heuristique qui possï¿½de plusieurs cas reprï¿½sentï¿½s ici
	 * par les variables donnï¿½es en paramï¿½tre : 
	 * 		- Colour + cand :
	 * 				 2 cases de la mï¿½me couleurs dans result ont la mï¿½me unitï¿½
	 * 		- Cell + cand : 
	 * 				1 cellule avec le canndidat cand possï¿½de est commune ï¿½ 
	 * 				deux cases dans result avec une couleur diffï¿½rente (testï¿½ 
	 * 				seulement dans le cas ou nb == 0 est faux donc que l'on a 
	 * 				une boucle)
	 * 		- Cell[] + cand : 
	 * 				Une rï¿½gion contient 2 cases de couleurs diffï¿½rentes et 
	 * 				d'autres cases possï¿½dant le candidat cand, ces derniers
	 * 				peuvent donc ï¿½tre supprimï¿½s.
	 */
	private Move setSolution(Colour col, String cand) {
		Move move = new Move();
		
		for (Cell c : result.keySet()) {
			if (result.get(c).equals(col)) {
				
					move.addAction(Move.ELIMINATE_CANDIDAT,c, cand);
				
				move.addReason( c);
			} else {
				move.addReason( c);
			}
		}
		move.setDetails("\n Coloring :\n"
			  + "Il est possible de supprimer les candidats " 
				+ cand.toString() + " des cases en " + col.getName() 
				+ ", car il y en a deux dans la meme unité");
		return move;
	}
	
	private Move setSolution(Cell cell, String cand) {
		Move move = new Move();
		
			move.addAction(Move.ELIMINATE_CANDIDAT,cell, cand);
		
		move.addReason( cell);
		
		for (Cell c : result.keySet()) {
			move.addReason( c);	
		}
		
		move.setDetails("\n Coloring :\n"
		+ "Il est possible de supprimer les candidats " + cand
				+ " Ã  l'intersection des deux couleurs");
		
		return move;
	}
	
	private Move setSolution(Cell[] cells, String cand) {
		Move move = new Move();
		
		for (Cell ci : cells) {
			
				move.addAction(Move.ELIMINATE_CANDIDAT,ci, cand);
		
			move.addReason( ci);
		}
		
		for (Cell c : result.keySet()) {
			move.addReason( c);	
		}
		
		move.setDetails("\n Coloring :\n"
		+ "Les candidats " + cand + " des cases en rouge "
				+ "peuvent etre supprimé car deux couleurs sont dans la meme"
				+ " unite.");
	
		return move;
	}
	
	// --- CASE 3 -------------------------------------------------------------
	
	/**
	 * Test le cas oï¿½ une rï¿½gion possede 2 cases de couleurs diffï¿½rentes et 
	 * 	d'autres cases avec le candidat cand.
	 */
	private Cell[] testResult3(Cell[] cells, String cand) {
		for (int i = 0; i < cells.length; ++i) {
			for (int j = i + 1; j < cells.length; ++j) {
				if (!result.get(cells[i]).equals(result.get(cells[j]))) {
					if (getNumberOfUnity(cells[i], Unity.REGION) 
							== getNumberOfUnity(cells[j], Unity.REGION)) {
						Cell[] res = getNewCellsOfRegion(getNumberOfUnity(cells[i], 
								Unity.REGION), cand);
						if (res != null) {
							return res;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Retourne toutes les cellules de la region numero nb qui n'appartiennent
	 *  pas ï¿½ result et qui possï¿½dent le candidat cand
	 */
	private Cell[] getNewCellsOfRegion(int nb, String cand) {
		Cell[] res = new Cell[Grid.size];
		int cpt = 0;
		for (int i = 0; i < Grid.size; ++i) {
			int x = getX(Unity.REGION, nb, i);
			int y = getY(Unity.REGION, nb, i);
			Cell ci = grid.getCellAt(x, y);
			if (!result.containsKey(ci) && ci.getCandidates() != null 
					&& ci.getCandidates().contains(cand)) {
				res[cpt] = ci;
				++cpt;
			}
		}
		
		if (cpt == 0) {
			return null;
		}
		Cell[] resCell = new Cell[cpt];
		for (int i = 0; i < res.length; ++i) {
			if (res[i] != null) {
				resCell[i] = res[i];
			}
		}
		
		return resCell;
	}
	
	// --- CAS 1 --------------------------------------------------------------
	
	/**
	 * Test si le contenu de cells permet de supprimer des candidats 
	 */
	private Colour testResult(Cell[] cells, String c) {
		// S'il n'y a pas au moins 5 cases, aucun croisement n'est possible
		if (result.keySet().size() <= MINIMUM_CELLS) {
			return null;
		}
		
		for (int i = 0; i < cells.length; ++i) {
			for (int j = i + 1; j < cells.length; ++j) {
				for (Unity unit : Unity.values()) {
					if (testBadCells(cells[i], cells[j], c, unit)) {
						return result.get(cells[i]);
					}	
				}
			}
		}
		return null;
	}
	
	/**
	 * Test si des cellules dans result avec la mï¿½me couleur ont une unitï¿½ 
	 * commune et si elles sont les seules ï¿½ possï¿½der ce candidat.
	 * Renvoie vrai si les cellules permettre d'avoir une heuristique vraie
	 */
	private boolean testBadCells(Cell c1, Cell c2, String cand, Unity u) {
		return result.get(c1).equals(result.get(c2)) 
				&& getNumberOfUnity(c1, u) == getNumberOfUnity(c2, u);
	}

	/**
	 * Commence par supprimer tous les ï¿½lements de la variable de classe
	 * result pour ensuite la remplir avec toutes les cases qui sont les seules
	 * de leur unitï¿½ ï¿½ posseder le candidat c ï¿½ partir des cellules stockï¿½es 
	 * dans la variable de classe start.
	 */
	private void fillResult(String c, Unity unit, int n) {
		result.clear();
		for (int i = 0; i < start.length; ++i) {
			Colour coul = Colour.values()[i];
			if (!result.containsKey(start[i])) {
				result.put(start[i], coul);
				fillResultWithCell(c, start[i], unit, coul.getNextColour());		
			}
		}
	}
	
	/**
	 * Parcours les cellules pour les ajouter dans rï¿½sultats lorsqu'elles 
	 * correspondent aux cellules voulut sur toutes les unitï¿½s en alternant
	 * la couleur ï¿½ enregistrer.
	 */
	private void fillResultWithCell(String cand, Cell c, Unity unit, Colour coul) {
		for (Unity u : Unity.values()) {
			if (!u.equals(unit)) {
				Cell res = testOnlyTwoCandidates(c, cand, u, getNumberOfUnity(c, u));
				if (res != null) {
					if (result.containsKey(res)) {
						return;
					}
					result.put(res, coul);
					fillResultWithCell(cand, res, u, coul.getNextColour());
				}
			}
		}
	}
	
	
	/**
	 * Permet d'obtenir le numï¿½ro de l'unitï¿½ ï¿½ partir d'une cellule et du 
	 * type de l'unitï¿½.
	 */
	private int getNumberOfUnity(Cell c, Unity unit) {
	
		switch (unit) {
		case LINE:
			return c.getCoordinate().getX();
		case COL:
			return c.getCoordinate().getY();
		case REGION:
			return (c.getCoordinate().getX() / Grid.REGION_SIZE) * Grid.REGION_SIZE 
					+ c.getCoordinate().getY() / Grid.REGION_SIZE;
		}
		return -1;
	}
	
	
	/**
	 * Test si l'unitï¿½ u numï¿½ro n possï¿½de seulement 2 candidats et si c'est le
	 * cas, renvoie la cellule diffï¿½rente de c
	 */
	private Cell testOnlyTwoCandidates(Cell cell, String c, Unity u, int n) {
		Cell res = null;
		int count = 0;
		for (int i = 0; i < Grid.size; ++i) {
			int x = getX(u, n, i);
			int y = getY(u, n, i);
			Cell ci = grid.getCellAt(x, y);
			if (ci.getCandidates() != null) {
				if (ci.getCandidates().contains(c)) {
					if (count == IDENTICALS_CELLS) {
						return null;
					}
					if (ci != cell) {
						res = ci;
					}
					++count;
				}
			}
		}
		return count == IDENTICALS_CELLS ? res : null;
	}
	
	
	/**
	 * Cette mï¿½thode test si une unitï¿½ ne possï¿½de que 2 cases avec le mï¿½me 
	 * candidat. Si c'est le cas, ces cases sont stockï¿½es dans la variable de
	 * classe start, sinon, start possï¿½dera des pointeurs nuls.
	 */
	private void onlyTwoCandidates(String c, Unity u, int n) {
		clearStart();
		int count = 0;
		for (int i = 0; i < Grid.size; ++i) {
			int x = getX(u, n, i);
			int y = getY(u, n, i);
			Cell ci = grid.getCellAt(x, y);
			if (ci.getCandidates() != null) {
				if (ci.getCandidates().contains(c)) {
					if (count == IDENTICALS_CELLS) {
						clearStart();
						return;
					}
					start[count] = ci;
					++count;
				}
			}
		}
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
	 * Test s'il est necessaire de remplir result ou non ï¿½ partir du contenu
	 * de start
	 */
	private boolean testStart() {
		for (Cell c : start) {
			if (c == null) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Supprime les pointeurs des variables dans start.
	 */
	private void clearStart() {
		for (int i = 0; i < start.length; ++i) {
			start[i] = null;
		}
	}
	

	
	// --- CAS 2 --------------------------------------------------------------
	
	/**
	 * Methode pour le cas 2 : 
	 * Permet de savoir si une cellule est sur la mï¿½me unitï¿½ (COL/LINE)
	 *  que 2 cases de couleurs diffï¿½rentes dans result
	 */
	private Cell testResult2(Cell[] cells, String c) {
		if (result.keySet().size() < MINIMUM_CELLS) {
			return null;
		}
	
		for (int i = 0; i < cells.length; ++i) {
			for (int j = i + 1; j < cells.length; ++j) {
				if (!result.get(cells[i]).equals(result.get(cells[j]))) {
					if (hasNotSameUnity(cells[i], cells[j])) {
						Cell ci = getInCommonCell(cells[i], cells[j], c);
						if (ci != null) {
							return ci;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Renvoie la cellule en commun de c1 et c2 possedant le candidat cand.
	 * Si cette cellule n'existe pas, renvoie null.
	 */
	private Cell getInCommonCell(Cell c1, Cell c2, String cand) {
		Cell ci = grid.getCellAt(c1.getCoordinate().getX(), 
				c2.getCoordinate().getY());
		if (!result.containsKey(ci) && ci.getCandidates() != null
				&& ci.getCandidates().contains(cand)) {
			return ci;
		}
		ci = grid.getCellAt(c2.getCoordinate().getX(), 
				c1.getCoordinate().getY());
		if (!result.containsKey(ci) && ci.getCandidates() != null
				&& ci.getCandidates().contains(cand)) {
			return ci;
		}
		return null;
	}
	
	/**
	 * Test si les cellules c1 et c2 ont une unitï¿½ en commun.
	 */
	private boolean hasNotSameUnity(Cell c1, Cell c2) {
		for (Unity u : Unity.values()) {
			if (getNumberOfUnity(c1, u) == getNumberOfUnity(c2, u)) {
				return false;
			}
		}
		return true;
}
		
	// CLASSES
	
	/**
	 * Reprï¿½sente les diffï¿½rentes couleurs que le coloriage va utiliser.
	 * Seulement 2 sont utiles pour cette heuristique.
	 */
	private enum Colour {
		GREEN("vert", Color.GREEN),
		BLUE("bleu", Color.BLUE);
		
		// ATTRIBUTS
		
		private String name;
		private Color color;
		
		// CONSTRUCTEURS
		
		Colour(String s, Color c) {
			name = s;
			color = c;
		}
		
		// REQUETES
		
		public Color getColor() {
			return color;
		}
		
		public String getName() {
			return name;
		}
		
		// COMMANDES
		
		public Colour getNextColour() {
			return Colour.values()[(this.ordinal() + 1) % Colour.values().length];
		}
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}
}
