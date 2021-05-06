package helpers;

public enum Localisation {
	LINE ("ligne"),
	COL ("colonne"),
	REGION ("region");
	
	// ATTRIBUTS
	
	private String name;
	
	// CONSTRUCTEURS
	
	private Localisation(String s) {
		name = s;
	}
	
	// REQUETES
	
	public String getName() {
		return name;
	}
}
