package helpers;

public enum Unity {
	LINE ("ligne"),
	COL ("colonne"),
	REGION ("region");
	
	// ATTRIBUTS
	
	private String name;
	
	// CONSTRUCTEURS
	
	private Unity(String s) {
		name = s;
	}
	
	// REQUETES
	
	public String getName() {
		return name;
	}
}
