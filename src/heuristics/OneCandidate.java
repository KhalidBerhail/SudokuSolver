package heuristics;


import cmd.Move;
import model.*;

public class OneCandidate  implements IHeuristic {

	
	private StdGrid grid;
	public OneCandidate(Grid grid) {
		this.grid=(StdGrid) grid;
		
	}
	/*
	 * Parcours de la grille 
	 * si la cellule contient un seul candidat
	 * cette candidat sera considérer comme valeur finale de la cellule 
	 * */
	@Override
	public Move getSolution() {
	
		Move move = new Move();

		for (int i = 0; i < Grid.size; ++i) {
			for (int j = 0; j < Grid.size; ++j) {
				Cell cell = this.grid.getCellAt(i, j);
				if (cell.getValue() == null && cell.getCandidates().size() == 1) {
					String value = this.grid.getCellAt(i, j).getCandidates().iterator().next();
					move.addAction(Move.SET_VALUE,cell, value);
					
					move.setDetails(getDescription(grid.getCellAt(i, j), value));
					
					grid.generateAllCandidat();
					return move;
				}
			}
		}
		return null;
	}
	
	/*
	 * Formulation du msg d'aide 
	 * */
	
	private String getDescription(Cell cell, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append(" OneCandidat :\n");
		sb.append("Le candidat '" + value + "'");
		sb.append(" est l'unique candidat possible pour la cellule de coordonnée ");
		sb.append("(" + (cell.getCoordinate().getX() + 1) + ";" + (cell.getCoordinate().getY() + 1) + ")");
		return sb.toString();
	}
	
	

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}


}
