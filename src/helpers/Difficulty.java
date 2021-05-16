package helpers;

public enum Difficulty {
	EASY("src\\puzzles\\EASY\\puzzle"+(int)(Math.random() * 2 + 1)+".xml"),
	MEDUIM("../puzzles/MEDUIM/puzzle1"),//+(int)(Math.random() * 5 + 1)),
	HARD("../puzzles/HARD/puzzle1");//+(int)(Math.random() * 5 + 1)),;
	private String path ;
	Difficulty(String string) {
		// TODO Auto-generated constructor stub
		path = string;
	}
	public String getPath() {
		return path;
	}
	
}