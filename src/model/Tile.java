package model;

//import
import java.io.FileReader;
import java.util.HashMap;

import javax.swing.JLabel;
/* Emma Shi 
 * Group 4 - Amazing Labryinth 
 * Date: Friday, April 26th, 2024
 * Tile Object
 */

//This class initializes the tile object
//This object is placed on the game board and contains 4 directions (up, down, left, right)
//It is also able to be rotated clockwise or counter-clockwise
public class Tile extends JLabel {

	// fields - to be retrieved from the FileReader class
	private String name;
	private String shape;
	private int rotation;
	private boolean moveable = true;
	private int x;
	private int y;
	private boolean isClockwiseClicked;
	private boolean isCounterClockwiseClicked;

	// constructor method
	public Tile(String name, String shape, int rotation, boolean moveable, int x, int y) {
		this.name = name;
		this.shape = shape;
		this.rotation = rotation; // validate
		this.moveable = moveable;
		this.x = x;
		this.y = y;
	}

	// getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public int getRotation() {
		return rotation;
	}

	// validate and assign the new rotation number
	public void setRotation(int rotation) {
		// only rotate if these tiles are moveable/able to be rotated
		if (moveable == true) {
			if (isClockwiseClicked == true) {
				this.rotation = rotateClockwise();
			} else if (isCounterClockwiseClicked == true) {
				this.rotation = rotateCounterClockwise();
			}

		}

		else
			this.rotation = rotation;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
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

	public void setClockwiseClicked(boolean isClicked) {
		this.isClockwiseClicked = isClicked;
	}

	public void setCounterClockwiseClicked(boolean isClicked) {
		this.isCounterClockwiseClicked = isClicked;
	}

	// Utility methods - public, to be accessed by other classes

	// This method rotates the tile counter-clockwise
	public int rotateCounterClockwise() {
		// Decrement each rotation to achieve a counter-clockwise rotation
		if (rotation != 0)
			return rotation - 1;
		// The rotation cannot be negative. Once it reaches 0, start back at rotation 3
		else
			return 3;
	}

	// this method rotates the tile clockwise
	public int rotateClockwise() {
		// Increment each rotation to achieve a clockwise rotation
		if (rotation != 3)
			return rotation + 1;
		// The rotation cannot exceed 3. After rotation 3, start back at rotation 0
		else
			return 0;

	}

	// Get the updated image file path for the free tile
	public String getImageFilePath() {
		return "images/" + getName() + getRotation() + ".png";
	}

	@Override
	public String toString() {
		return "Tile [name=" + name + ", shape=" + shape + ", rotation=" + rotation + ", moveable=" + moveable + ", x="
				+ x + ", y=" + y + ", isClockwiseClicked=" + isClockwiseClicked + ", isCounterClockwiseClicked="
				+ isCounterClockwiseClicked + "]";
	}
	
	

}
