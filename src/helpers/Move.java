package helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Cell;

public class Move {
	public static String ELIMINATE_CANDIDAT = "eleminate";
	public static String SET_VALUE = "set_value"; 


	
	private Map<String, MyMap<String,Cell> > actions ;
	private List<Cell> reasons ; 
	private String details = null ; 
	
	  public Move() {
		// TODO Auto-generated method stub
		actions = new HashMap<String, MyMap<String,Cell>>();
		reasons  = new ArrayList<Cell>();
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
	public void act() {
	
		for(String v :actions.keySet()) {
			
			for(Node<String,Cell> node :actions.get(v).getMap()) {
				if(v==ELIMINATE_CANDIDAT) {
					 node.getValue();
					
					node.getValue().eliminateCandidate(node.getKey());}
				if(v==SET_VALUE) {
					node.getValue().setValue(node.getKey());
				    node.getValue().lock();
				    node.getValue().getGrid().generateAllCandidat();
				    }
			} 
		} 
		
		
	}
	
}