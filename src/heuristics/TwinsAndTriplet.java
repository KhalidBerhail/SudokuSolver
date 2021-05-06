package heuristics;

import java.util.ArrayList;
import java.util.List;

import cmd.Move;
import model.Cell;
import model.Coordinate;
import model.Grid;
import model.StdGrid;

public class TwinsAndTriplet  implements IHeuristic {

	private Grid grid;
	public TwinsAndTriplet(Grid grid) {
		this.grid=grid;
	}

	@Override
	public Move getSolution() {
		
		Move move = new Move();
		int size = 3;
		String solution= null;
		StringBuilder sb = new StringBuilder();
		// Pour tout les candidats
		for (String c : StdGrid.defaultValueSet()) {
			
			// Pour tout les triplet(al, be, ga) de région alignées en ligne
			for (int k = 0; k < size; ++k) {
				int[] regions = new int[size];
				for (int j = 0; j < size; ++j) {
					regions[j] = k * size + j;
				}
				
				// Pour chaque région de la ligne
				for (int numRegion : regions) {
					
					// Décalage lié à la région
					int shiftX = (numRegion % size) * size;
					int shiftY = (numRegion / size) * size;
					List<Cell> cellFromRegion = new ArrayList<Cell>();
					
					// Stocke si ligne de la région contiens la valeur
					for (int i = 0; i < size; ++i) {
						for (int j = 0; j < size; ++j) {
							Cell cell = grid.getCellAt(i + shiftX, j + shiftY);
							if (cell.getCandidates() != null &&
									cell.getCandidates().contains(c)) {
								cellFromRegion.add(cell);
							}
						}
					}

					// Vérification si la région est correct (renvoie -1 si faux);
					int numLine = regionLineIsCorrect(cellFromRegion, size);
					// Si la région est correct (le candidat sur une seule ligne de la région
					if (numLine != -1) {
						List<Cell> cellCorrectY = new ArrayList<Cell>();
						
						// On regarde sur les autres régions sur la même ligne si des
						//	des cellules contiennent la valeur en candidat également.
						//	Si c'est le cas, on les met dans cellCorrectY
						for (int j = 0; j < size * size; ++j) {
							Cell cell = grid.getCellAt(j, numLine);
							if (!cellFromRegion.contains(cell) && cell.getCandidates() != null && cell.getCandidates().contains(c)) {
								cellCorrectY.add(cell);
							}
						}
						
						if (cellCorrectY.size() > 0) {
							//Solution sol = new StdSolution();
							//SetSolution((StdSolution) sol, cellFromRegion, cellCorrectY, c, size, true);
							Coordinate cor=null;
							
							for (Cell cell : cellCorrectY) {
								//solution.addReason(Color.ORANGE, cell);
								cor=cell.getCoordinate();
								move.addAction(Move.ELIMINATE_CANDIDAT, cell, c);
								//grid.getGrid()[cor.getX()][cor.getY()].eliminateCandidate(c);
								//solution="remove cell :candidat :"+c+"from cell:"+cell.toString()+"\n";
								
						}
							for (Cell cell : cellFromRegion) {
								//solution+="à cause de cell :"+cell.toString()+"\n";
								move.addReason(cell);
							}
							Coordinate tmp = cellFromRegion.get(0).getCoordinate();
							int num = //false <-- X
									tmp.getX() ;
									
							 numRegion = (tmp.getX() / size) + (tmp.getY() / size) + 1;
							/*StringBuilder sb = new StringBuilder();
							sb.append("twins and Triplet\n");
							sb.append("Le candidat '" + c + "'");
							sb.append(" n'étant présent uniquement sur la ");
							sb.append("ligne");
							sb.append(" numéro " + (num + 1));
							sb.append(" dans la région numéro " + numRegion);
							sb.append(", alors on peut retirer tout autres mêmes candidats sur la meme ");
							sb.append("ligne");
							solution+=sb.toString();*/
							 sb.append(getDescription(num, numRegion, c));
							 
						}
					}
				}
			}
			
			// Pour tout les triplet(al, be, ga) de région alignées en colonne
			for (int k = 0; k < size; ++k) {
				int[] regions = new int[size];
				for (int j = 0; j < size; ++j) {
					regions[j] = j * size + k;
				}
				
				// Pour chaque région de la ligne
				for (int numRegion : regions) {
					
					// Décalage lié à la région
					int shiftX = (numRegion % size) * size;
					int shiftY = (numRegion / size) * size;
					List<Cell> cellFromRegion = new ArrayList<Cell>();
					
					// Stocke si ligne de la région contiens la valeur
					for (int i = 0; i < size; ++i) {
						for (int j = 0; j < size; ++j) {
							Cell cell = grid.getCellAt(j + shiftX, i + shiftY);
							if (cell.getCandidates() != null &&
									cell.getCandidates().contains(c)) {
								cellFromRegion.add(cell);
							}
						}
					}

					// Vérification si la région est correct (renvoie -1 si faux);
					int numColumn = regionColumnIsCorrect(cellFromRegion, size);
					// Si la région est correct (le candidat sur une seule ligne de la région
					if (numColumn != -1) {
						List<Cell> cellCorrectX = new ArrayList<Cell>();
						
						// On regarde sur les autres régions sur la même ligne si des
						//	des cellules contiennent la valeur en candidat également.
						//	Si c'est le cas, on les met dans cellCorrectY
						for (int j = 0; j < size * size; ++j) {
							Cell cell = grid.getCellAt(numColumn, j);
							if (!cellFromRegion.contains(cell) && cell.getCandidates() != null && cell.getCandidates().contains(c)) {
								cellCorrectX.add(cell);
							}
						}
						
						if (cellCorrectX.size() > 0) {
							
						//	SetSolution((StdSolution) sol, cellFromRegion, cellCorrectX, c, size, false);
							Coordinate cor=null;
							for (Cell cell : cellCorrectX) {
								//solution.addReason(Color.ORANGE, cell);
								move.addAction(Move.ELIMINATE_CANDIDAT, cell, c);
								//cor=cell.getCoordinate();
								//grid.getGrid()[cor.getX()][cor.getY()].eliminateCandidate(c);
								//solution="remove cell :"+cell.toString()+"\n";
								
						}
							for (Cell cell : cellFromRegion) {
								move.addReason(cell);
								//solution+="à cause de cell :"+cell.toString()+"\n";
							}
							Coordinate tmp = cellFromRegion.get(0).getCoordinate();
							int num = //false <-- X
									tmp.getY() ;
									
							 numRegion = (tmp.getX() / size) + (tmp.getY() / size) + 1;
							 sb.append(getDescription(num, numRegion, c));
							/*StringBuilder sb = new StringBuilder();
							sb.append("twins and Triplet\n");
							sb.append("Le candidat '" + c + "'");
							sb.append(" n'étant présent uniquement sur la ");
							sb.append("colonne");
							sb.append(" numéro " + (num + 1));
							sb.append(" dans la région numéro " + numRegion);
							sb.append(", alors on peut retirer tout autres mêmes candidats sur la m½me ");
							sb.append("colonne");
							solution+=sb.toString();*/
					}
				}
			}
		}
		}
		if(move.getActions().size()>0) {
		
		move.setDetails(sb.toString());
		
		return move;}
		
		else return null;
	}
	private String getDescription (int num,int numRegion,String c) {
		StringBuilder sb = new StringBuilder();
		sb.append("twins and Triplet\n");
		sb.append("Le candidat '" + c + "'");
		sb.append(" n'étant présent uniquement sur la ");
		sb.append("ligne");
		sb.append(" numéro " + (num + 1));
		sb.append(" dans la région numéro " + numRegion);
		sb.append(", alors on peut retirer tout autres mêmes candidats sur la meme ");
		sb.append("ligne");
		return sb.toString();
	}
	

	
	private int regionColumnIsCorrect(List<Cell> cells, int size) {
		int numLine = -1;
		for (Cell cell : cells) {
			if (numLine == -1) {
				numLine = cell.getCoordinate().getX();
			} else {
				if (cell.getCoordinate().getX() != numLine) {
					return -1;
				}
			}
		}
		return (cells != null && cells.size() > 1 ? numLine : -1);
	}
	
	
	private int regionLineIsCorrect(List<Cell> cells, int size) {
		int numLine = -1;
		for (Cell cell : cells) {
			if (numLine == -1) {
				numLine = cell.getCoordinate().getY();
			} else {
				if (cell.getCoordinate().getY() != numLine) {
					return -1;
				}
			}
		}
		return numLine;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}