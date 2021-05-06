package componentsModel;


/*import java.util.ArrayList;
import java.util.List;*/
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;
import java.util.TreeSet;

public class TextFieldModel {
	
   private boolean canEdit;
   private Set<String> candidats;
   private PropertyChangeSupport pcs;
   
   public TextFieldModel() {
	   canEdit = false;
	   candidats = new TreeSet<String>();
	   pcs = new PropertyChangeSupport(this);
   }
   
   public void setCanEdit(boolean val) {
	   canEdit = val;
   }
   
   public boolean canEdit() {
	return canEdit;
   }
   
   public void setCandidats(Set<String> set) {
	   candidats.clear();
	   candidats.addAll(set);
	   pcs.firePropertyChange("CANDIDATS", null, set);
   }
   
   public void removeCandidat(String s) {
	   candidats.remove(s);
	   pcs.firePropertyChange("CANDIDAT_ELIMINATED", null, s);
   }
   public Set<String> getCandidats(){
	 return candidats;  
   }

   public void addPropertyChangeListener(String propName, PropertyChangeListener PL) {
	   if(PL!=null) {
		   pcs.addPropertyChangeListener(propName,PL);
	   }
   }
   
   public void removePropertyChangeListener(String propName, PropertyChangeListener PL) {
	   if(PL!=null) {
		   pcs.removePropertyChangeListener(propName, PL);
	   }
   }
}
