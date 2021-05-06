package componentsModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PaneModel {
	
  private boolean selected;
  private boolean hovered;
  private PropertyChangeSupport pcs;
  
  public PaneModel() {
	  selected = false;
	  pcs = new PropertyChangeSupport(this);
  }
  
  public boolean isSelected() {
	  return selected;
  }
  
  public void setSelected(boolean val) {
	  boolean oldVal = selected;
	  selected = val;
	  pcs.firePropertyChange("SELECTED", oldVal, val);
  }
  
  public boolean isHovered() {
	  return hovered;
  }
  
  public void setHovered(boolean val) {
	  boolean oldVal = hovered;
	  hovered = val;
	  pcs.firePropertyChange("HOVERED", oldVal, val);
  }
  
  public void addPropertyChangeListener(String propName, PropertyChangeListener pcl) {
	  if(pcl!=null) {
		  pcs.addPropertyChangeListener(propName, pcl);
	  }
  }
  
  public void removePropertyChangeListener(String propName, PropertyChangeListener pcl) {
	  if(pcl!=null) {
		  pcs.removePropertyChangeListener(propName, pcl);
	  }
  }
}
