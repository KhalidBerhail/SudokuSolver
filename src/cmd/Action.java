package cmd;

import java.util.List;
import java.util.Map;

import helpers.MyMap;
import model.Cell;

/**
 * Définis une action.
 * Une action est exécuté sur une cellule, et contient une
 * 	valeur.
 * La sémantique de l'action ne pourra être complète que dans les classes
 *  qui implémenteront cette interface.
 * @inv <pre>
 *     getState() != null
 *     getCell() != null
 *     canDo() ==> getState() == State.DO
 *     canUndo() ==> getState() == State.UNDO </pre>
 * @author cleme
 */
public interface Action {
	
	/**
	 * Renvoie la cellule correspondante à l'action
	 * @return cell
	 * @post
	 * 		getCell() != null
	 */
	void setReasons(List<Cell> reasons) ;
	
	/**
	 * Renvoie la valeur correspondante à l'action.
	 * @return
	 */
	 void addReason(Cell cell) ;
	 
	 
	  void addAction(String action , Cell cell,String val );
	  
	  void setDetails(String details);
	  
	  Map<String, MyMap<String,Cell>> getActions();
	  List<Cell> getReasons() ;
	  
	  String getDetails();

	
	/**
	 * Renvoie l'état actuel de l'action
	 * @return State
	 * @post
	 * 		getState() == State.DO || 
	 * 			getState() == State.UNDO
	 */
	State getState();
	
	/**
	 * Renvoie vrai ssi l'action peut être faite.
	 * @return boolean
	 * @post
	 * 		canDo() <=> getState() == State.DO
	 */
	boolean canDo();

	/**
	 * Renvoie vrai ssi l'action peut être défaite.
	 * @return boolean
	 * @post
	 * 		canDo() <=> getState() == State.UNDO
	 */
	boolean canUndo();
	
	/**
	 * Fais l'action.
	 * La méthode sera mieux défini dans les classes l'implémentant.
	 */
	void act();
}
