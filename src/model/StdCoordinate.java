package model;

import helpers.Contract;

public class StdCoordinate implements Coordinate {
	
	private int x;
	private int y;
	
	public StdCoordinate(int x, int y) {
		Contract.checkWrongCondition(x < 0 || y < 0, "Cordonées !");
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(row: ");
		sb.append(getX());
		sb.append(", col: ");
		sb.append(getY());
		sb.append(")");
		return sb.toString();
	}
}