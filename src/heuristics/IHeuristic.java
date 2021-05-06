package heuristics;

import cmd.Move;

public interface IHeuristic {
	
	// REQUETES
	
	/**
	 * Indique le niveau de difficultée de l'heuristique
	 * @return
	 */
	
    int getLevel();

	// COMMANDES

	/**
	 * Retourne la solution trouvée par l'heuristique sur la grille grid
	 */
	Move getSolution();

}
