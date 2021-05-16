package cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.MyMap;
import helpers.Node;
import model.Cell;

public class Move extends AbstractAction implements Action {


		
		public Move() {
		super();
		// TODO Auto-generated constructor stub
	}
		public void setReasons(List<Cell> reasons) {
			this.reasons = reasons;
		}
	  public void addReason(Cell cell) {
		  reasons.add(cell);
	  }
	  public void addAction(String action , Cell cell,String val ) {
		  if(actions.size()==0) {
			 
			  MyMap<String,Cell> mapK = new MyMap<String,Cell>();
			  mapK.put( val,cell);
			  actions.put(action, mapK);
		  }else {
			  actions.get(action).put(val, cell);
		  }
		
		  
	  }
	  public void setDetails(String details) {
		  this.details = details;
	  }
	public Map<String, MyMap<String,Cell>> getActions() {
		return actions;
	}
	
	public List<Cell> getReasons() {
		return reasons;
	}
	public String getDetails() {
		return details;
	}
	public void doIt() {
	
		for(String v :actions.keySet()) {
			
			for(Node<String,Cell> node :actions.get(v).getMap()) {
				switch (v) {
				case ELIMINATE_CANDIDAT: {
					 
					node.getValue().eliminateCandidate(node.getKey());
					break;
				}
				case SET_VALUE: {
					node.getValue().setValue(node.getKey());
				    node.getValue().lock();
				    node.getValue().getGrid().generateAllCandidat();
				    break;
				}
				case REMOVE_VALUE: {
					node.getValue().removeValue();
					node.getValue().getGrid().generateAllCandidat();
				   break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + v);
					
				}
				
				
				
			} 
		} 
		
		
	}

	
	
	

	@Override
	protected void undoIt() {
		// TODO Auto-generated method stub
		for(String v :actions.keySet()) {
			
			for(Node<String,Cell> node :actions.get(v).getMap()) {
				switch (v) {
				case ELIMINATE_CANDIDAT: {
					
					node.getValue().addCandidate(node.getKey());
					break;
				}
				case SET_VALUE: {
					node.getValue().removeValue();
				    break;
				}
				case REMOVE_VALUE: {
					node.getValue().setValue(node.getKey());
				    node.getValue().lock();
				    node.getValue().getGrid().generateAllCandidat();
				   break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + v);
					
				}
			
			} 
		} 
	}
	
	 public Cell getCell() {
		 if(actions.containsKey(REMOVE_VALUE)) {
			 return actions.get(REMOVE_VALUE).getMap().get(0).getValue();
		 }
		 if(actions.containsKey(SET_VALUE)) {
	        return actions.get(SET_VALUE).getMap().get(0).getValue();
		 }
		 else return null;
	    }
	
}
