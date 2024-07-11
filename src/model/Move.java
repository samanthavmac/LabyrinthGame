package model;

/* 
 * Name: Emma Shi 
 * Move Class
 * Group 4
 * Date: May 6th, 2024
 */

// This class stores information about the AI player's move
public class Move {

	// Fields
	private int row; // row to insert piece
	private int col; // col to insert piece
	private int rotation; // rotation to insert piece in
	private int final_x; // final x/row position to go to after making move
	private int final_y; // final y/col position to go to after making move
	private Player player;

	// Constructor
	public Move(int row, int col, int rotation) {
		setRow(row);
		setCol(col);
		setRotation(rotation);
	}

	// Move to insert a tile to block player
	// Overloaded constructor
	public Move(int row, int col, int rotation, Player player) {
		this.row = row;
		this.col = col;
		this.rotation = rotation;
		this.player = player;

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return "Move [row=" + row + ", col=" + col + ", rotation=" + rotation + "]";
	}

	public int getFinal_x() {
		return final_x;
	}

	public void setFinal_x(int final_x) {
		this.final_x = final_x;
	}

	public int getFinal_y() {
		return final_y;
	}

	public void setFinal_y(int final_y) {
		this.final_y = final_y;
	}

}
