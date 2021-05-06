package heuristics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cmd.Move;
import helpers.Unity;
import model.Cell;
import model.Grid;
import model.StdGrid;

public class MixedGroups  implements IHeuristic {
	
	// CONSTANTES 
	
	private static final int MIN_SIZE = 2;
	private static final int MAX_SIZE = 4;
	private StdGrid grid;
	// ATTRIBUTS
	
	private Map<String, List<Cell>> map;
	
	// CONSTRUCTEURS
	
	public MixedGroups(Grid grid) {
		this.grid=(StdGrid) grid;
		map = new LinkedHashMap<String, List<Cell>>();
		for (String s : this.grid.defaultValueSet()) {
			map.put(s, new ArrayList<Cell>());
		}
	}

	// REQUETES
	
	@Override
	public Move getSolution() {
	 
		for (Unity u : Unity.values()) {
			for (int i = 0; i < Grid.size; ++i) {
				for (int j = 0; j < Grid.size; ++j) {
					map.clear();
					if (this.grid.getCellAt(i, j).getValue() == null) {
						fillMap(i, j, u);
					}
					if (!map.isEmpty()) {
						for (int n = 2; n <= MAX_SIZE; ++n) {
							String[] res = tryUnite(n);	
							if (res != null) {
								if (testSolution(res)) {
									return setSolution(res, u, getNb(i, j, u) + 1);
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
	
	private Move setSolution(String[] val, Unity loc, int nb) {
		
		Move move = new Move();
		Cell[] unite = calcUnite(val);
		
		
		for (Cell c : unite) {
			move.addReason(c);
			if (c.getCandidates() != null) {
				for(Iterator<String> candidat= c.getCandidates().iterator();candidat.hasNext();) {
					String cand= candidat.next();
					if (!tabContains(val, cand)) {					
						move.addAction(Move.ELIMINATE_CANDIDAT,c,cand);	
							//this.grid.getCellAt(c.getCoordinate().getX(),c.getCoordinate().getY()).eliminateCandidate(cand);
					}
				}		
				
			}
		}
	
		move.setDetails(setDescription(loc, val, nb));
				return move;
	}
	
	private String setDescription(Unity loc, String[] val, int nb) {
		StringBuilder res = new StringBuilder(); 
		res.append(" \n Groupe Mélanger :\n");
		res.append("Les " + val.length + " candidats : ");
		for (String s : val) {
			res.append(s + " ");
		}
		res.append("sont présents seuls dans " + val.length 
				+ " cases sur la " + loc.getName() + " numero " + nb
				+ ", il est donc possible de supprimer"
				+ " les autres candidats dans ces cases.");
		return res.toString();
	}
	private boolean tabContains(String[] tab, String val) {
		for (String si : tab) {
			if (si.equals(val)) {
				return true;
			}
		}
		return false;
	}

	private String[] tryUnite(int n) {
		String[] tab = new String[Grid.size];
		int count = 0;
		for (String s : map.keySet()) {
			if (map.get(s) != null) {
				if (map.get(s).size() <= n && map.get(s).size() >= MIN_SIZE) {
					tab[count] = s;
					++count;
				}
			}
		}
		if (count == 0) {
			return null;
		}
		String[] res = new String[count];
		count = 0;
		while (count < tab.length && tab[count] != null) {
			res[count] = tab[count];
			++count;
		}
		if (res.length < n) {
			return null;
		}
		return unite(res, n);
	}
	
	private String[] unite(String[] tab, int n) {
		String[][] ens = new String[(int) Math.pow(tab.length, n)][n];
		int count = 0;
		switch (n) {
		case 2:
			for (int i = 0; i < tab.length; ++i) {
				for (int j = i + 1; j < tab.length; ++j) {
					ens[count][0] = tab[i];
					ens[count][1] = tab[j];
					count++;
				}
			}
			break;
		case 3:
			for (int i = 0; i < tab.length; ++i) {
				for (int j = i + 1; j < tab.length; ++j) {
					for (int k = j + 1; k < tab.length; ++k) {
						ens[count][0] = tab[i];
						ens[count][1] = tab[j];
						ens[count][2] = tab[k];
						count++;
					}
				}
			}
			break;
		case 4:
			for (int i = 0; i < tab.length; ++i) {
				for (int j = i + 1; j < tab.length; ++j) {
					for (int k = j + 1; k < tab.length; ++k) {
						for (int m = k + 1; m < tab.length; ++m) {
							ens[count][0] = tab[i];
							ens[count][1] = tab[j];
							ens[count][2] = tab[k];
							ens[count][3] = tab[m];
							count++;
						}
					}
				}
			}
			break;
		case 5:
			for (int i = 0; i < tab.length; ++i) {
				for (int j = i + 1; j < tab.length; ++j) {
					for (int k = j + 1; k < tab.length; ++k) {
						for (int m = k + 1; m < tab.length; ++m) {
							for (int nn = m + 1; nn < tab.length; ++nn) {
								ens[count][0] = tab[i];
								ens[count][1] = tab[j];
								ens[count][2] = tab[k];
								ens[count][3] = tab[m];
								ens[count][4] = tab[nn];
								count++;
							}
						}
					}
				}
			}
			break;
		case 6:
			for (int i = 0; i < tab.length; ++i) {
				for (int j = i + 1; j < tab.length; ++j) {
					for (int k = j + 1; k < tab.length; ++k) {
						for (int m = k + 1; m < tab.length; ++m) {
							for (int nn = m + 1; nn < tab.length; ++nn) {
								for (int o = nn + 1; o < tab.length; ++o) {
									ens[count][0] = tab[i];
									ens[count][1] = tab[j];
									ens[count][2] = tab[k];
									ens[count][3] = tab[m];
									ens[count][4] = tab[nn];
									ens[count][5] = tab[o];
									count++;	
								}
							}
						}
					}
				}
			}
			break;
		default:
			return null;
		}
		for (String[] st : ens) {
			if (calcUnite(st).length == n) {
				return st;
			}
		}
		return null;
	}
	
	private Cell[] calcUnite(String[] tab) {
		Set<Cell> res = new HashSet<Cell>();
		for (String s : tab) {
			if (s != null) {
				for (Cell c : map.get(s)) {
					if (!res.contains(c)) {
						res.add(c);
					}
				}
			}
		}
		Object[] ol = res.toArray();
		Cell[] s = new Cell[res.size()];
		for (int i = 0; i < s.length; ++i) {
			s[i] = (Cell) ol[i];
		}
		return s;
	}
	
	private boolean testSolution(String[] val) {
		Cell[] union = calcUnite(val);
		for (Cell c : union) {
			if (c.getCandidates() != null) {
				for (String cand : c.getCandidates()) {
					if (!tabContains(val, cand)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void fillMap(int line, int col, Unity unity) {
		for (String s : grid.defaultValueSet()) {
			map.put(s, new ArrayList<Cell>());
		}
		int x;
		int y;
		for (int i = 0; i < Grid.size; ++i) {
			switch (unity) {
				case LINE:
					x = line;
					y = i;
					break;
				case COL:
					x = i;
					y = col;
					break;
				case REGION:
					x = line /Grid.REGION_SIZE *Grid.REGION_SIZE + (i / Grid.REGION_SIZE);
					y = col/Grid.REGION_SIZE * Grid.REGION_SIZE+ (i % Grid.REGION_SIZE);
					break;
				default:
					return;
			}
			Cell c = this.grid.getCellAt(x, y);
			if (c.getCandidates() != null) {
				for (String s : c.getCandidates()) {
					map.get(s).add(c);
				}
			}
		}
	}
	
	
	
	private int getNb(int i, int j, Unity u) {
		switch(u) {
		case LINE:
			return i;
		case COL:
			return j;
		case REGION:
			return ((i/Grid.size) + j )/ Grid.REGION_SIZE;
		}
		return -1;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}