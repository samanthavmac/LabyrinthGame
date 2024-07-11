package model;

/* 
 * Name: Emma Shi 
 * Grid State
 * Group 4
 * Date: May 5th, 2024
 */

// This class contains information about the current state of the grid
public class GridState {

	// Fields
	int current_x;
	int current_y;
	Tile[][] grid;

	// Constructor
	public GridState(int x, int y, Tile[][] grid) {
		setCurrent_x(x);
		setCurrent_y(y);
		setGrid(grid);
	}

	// Getters and setters
	public Tile[][] getGrid() {
		return grid;
	}

	public void setGrid(Tile[][] grid) {
		this.grid = grid;
	}

	public int getCurrent_x() {
		return current_x;
	}

	public void setCurrent_x(int current_x) {
		this.current_x = current_x;
	}

	public int getCurrent_y() {
		return current_y;
	}

	public void setCurrent_y(int current_y) {
		this.current_y = current_y;
	}

}
