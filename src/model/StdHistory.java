package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Collections;
import java.util.Queue;

public class StdHistory<E> implements History<E> {
	
	// ATTRIBUTS
	
	private int maxHeight;
	private PropertyChangeSupport pcs;

	private Deque<E> head;
	private Queue<E> tail;
	
	// CONSTRUCTEUR
	
	public StdHistory(int maxHeight) {
		if (maxHeight <= 0) {
			throw new AssertionError("Constructor <= 0 : StdHistory");
		}
		this.maxHeight = maxHeight;
		this.head = new ArrayDeque<E>();
		this.tail = Collections.asLifoQueue(new ArrayDeque<E>());
		pcs = new PropertyChangeSupport(this);
	}
	
	// REQUETES
	
	@Override
	public int getMaxHeight() {
		return this.maxHeight;
	}
    
	@Override
   public int getCurrentPosition() {
		return this.head.size();
	}
   
	@Override
   public E getCurrentElement() {
		if (this.getCurrentPosition() <= 0) {
			throw new AssertionError("getCurrentElement <= 0 : StdHistory");
		}
		return this.head.getFirst();
	}
	
	@Override
	public int getEndPosition() {
		return this.head.size() + this.tail.size();
	}
	
	private boolean isFull() {
		return (this.head.size() == this.maxHeight);
	}
	
	private boolean tailIsEmpty() {
		return (this.tail.size() == 0);
	}
	
	// COMMANDES
	
	@Override
	public void clearAll() {
		head.clear();
		tail.clear();
		checkPCL();
	}
	
	@Override
    public void add(E e) {
		if (e == null) {
			throw new AssertionError("add null: StdHistory");
		}
		if (this.isFull()) {
			head.removeLast();
		}
		if (!tailIsEmpty()) {
			this.tail.clear();
		}
		head.addFirst(e);
		checkPCL();
	}
	
	@Override
    public void goForward() {
		if (this.getCurrentPosition() >= this.getEndPosition()) {
			throw new AssertionError("goForward error position: StdHistory");
		}
		this.head.addFirst(this.tail.remove());
		checkPCL();
	}
	
	@Override
    public void goBackward() {
		if (this.getCurrentPosition() <= 0) {
			throw new AssertionError("goForward error position: StdHistory");
		}
		this.tail.add(this.head.removeFirst());
		checkPCL();
	}
	
	private void checkPCL() {
		pcs.firePropertyChange(PROP_UNDO, null, getCurrentPosition() > 0);
		pcs.firePropertyChange(PROP_REDO, null, getCurrentPosition() < getEndPosition());
	}

	// LISTENERS
	
	@Override
	public void addPropertyChangeListener(String pName, PropertyChangeListener listener) {
		if (listener != null) {
			pcs.addPropertyChangeListener(pName, listener);
		}
	}

	@Override
	public void removePropertyChangeListener(String pName, PropertyChangeListener listener) {
		if (listener != null) {
			pcs.removePropertyChangeListener(pName, listener);
		}
	}
	
	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return pcs.getPropertyChangeListeners();
	}
	
	@Override
	public PropertyChangeListener[] getPropertyChangeListeners(String name) {
		return pcs.getPropertyChangeListeners(name);
	}
}
