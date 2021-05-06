package heuristics;

import cmd.Move;

public interface IHeuristic {
	
	// REQUETES
	
	/**
	 * Indique le niveau de difficult�e de l'heuristique
	 * @return
	 */
	
    int getLevel();

	// COMMANDES

	/**
	 * Retourne la solution trouv�e par l'heuristique sur la grille grid
	 */
	Move getSolution();

}
