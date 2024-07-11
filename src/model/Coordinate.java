package model;

/*
 * Labyrinth Game
 * Coordinate
 * Group 4
 * Samantha Mac
 * April 22, 2024
 */


// This class creates a coordinate point to track player movements
public class Coordinate {
	// Fields
	private int x;
	private int y;
	
	// Constructor
	public Coordinate(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Coordinate [x=" + x + ", y=" + y + "]";
	}
	
	
}
