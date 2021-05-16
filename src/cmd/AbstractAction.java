package cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.MyMap;
import model.Cell;

/**
 * Définis le code principal d'une action.
 * Chaque action a un état et l'action effectué dépend
 * 	de l'état de l'action.
 * Si getState() == State.DO alors, exécutera {@link #doIt()}
 * Si getState()} == State.UNDO alors, exécutera {@link #undoIt()}
 * Les méthodes doivent être redéfinis dans les classes extendant
 * 	cette classe abstraite.
 * @author cleme
 * @construtor
 * 		$DESC$ Créer une action sur une cellule et une valeur.
 * 		$ARGS$ Cell cell, String value
 * 		$PRE$
 * 			cell != null
 * 		$POST$
 * 			getCell() == cell;
 * 			getValue() == value
 * 			getState() == State.DO
 */
public abstract class AbstractAction implements Action {
	
	// ATTRIBUTS
	public static final String ELIMINATE_CANDIDAT = "eleminate";
	public static final String SET_VALUE = "set_value"; 
	public static final String REMOVE_VALUE = "remove_value";
	protected Map<String, MyMap<String,Cell> > actions ;
	protected List<Cell> reasons ; 
	protected String details = null ; 
	protected State state;
	
	// CONSTRUCTEUR
	
	public AbstractAction() {
		actions = new HashMap<String, MyMap<String,Cell>>();
		reasons  = new ArrayList<Cell>();
		this.state = State.DO;
	}
	
	

	@Override
	public State getState() {
		return this.state;
	}

	@Override
	public boolean canDo() {
		return state == State.DO;
	}

	@Override
	public boolean canUndo() {
		return state == State.UNDO;
	}
	
	/**
	 * Acte l'action.
	 * @post
	 * 		(old getState()) == State.DO =>
	 * 			getState() == State.UNDO
	 * 			{@link #doIt()} est appelé}
	 * 		(old getState()) == State.UNDO =>
	 * 			getState() == State.DO
	 * 			{@link #undoIt()} est appelé}
	 */
	@Override
	public final void act() {
	    if (!canDo() && !canUndo()) {
	    	throw new AssertionError("act no state : AbstractAction");
	    }
	
	    if (canDo()) {
	        doIt();
	        state = State.UNDO;
	    } else { // nÃ©cessairement canUndo() == true
	        undoIt();
	        state = State.DO;
	    }
	}
    
    protected abstract void doIt();
    protected abstract void undoIt();

}
