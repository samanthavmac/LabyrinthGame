package controller;

//Imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Tile;
import model.TileMap;

/*
 * Name: Emma Shi
 * AI Move Controller Class
 * Group 4
 * Emma Shi
 * Date: May 5th, 2024
 */

// This class validates all AI player movements and displays them
public class AIMoveController {

	// Fields
	private Tile[][] grid;
	private int x;
	private int y;

	// Track number of moves
	private int numMoves = 0;

	// Store maps/grids of all possible tiles
	public ArrayList<TileMap> maps = new ArrayList<TileMap>();

	// Tile user clicked
	private int destinationRow;
	private int destinationCol;

	// Computer wants to find valid paths
	private boolean findingValidPaths;
	// Counter
	public static int validPaths = 0;

	// Initialize variables about priority of movements
	private static final int UP = 0;
	private static final int RIGHT = 1;
	private static final int DOWN = 2;
	private static final int LEFT = 3;

	// Use character map to track movements
	public char[][] path = new char[7][7];

	private boolean success;

	public AIMoveController(int destinationRow, int destinationCol, boolean findingValidPaths, int x, int y,
			Tile[][] grid) {

		// Initialize fields
		this.destinationRow = destinationRow;
		this.destinationCol = destinationCol;
		this.findingValidPaths = findingValidPaths;

		this.x = x;
		this.y = y;
		this.grid = grid;

		// Generate boolean maps of tiles
		generateMaps();
		// Create a character grid to track player's path
		pathTracking();
		// Mark where the player starts
		path[getX()][getY()] = 'X';
		// Find a solution to player's destination
		setSuccess(traverseBoard(getX(), getY()));

		for (int i = 0; i < path.length; i++) {
			for (int j = 0; j < path.length; j++) {
				System.out.print(path[i][j] + " ");
			}
			System.out.println();
		}
	}

	// Create a 3 by 3 visualization of each tile object
	private void generateMaps() {
		// If txt file exists/ is accessible
		try {
			// Read txt using Scanner
			Scanner inputFile = new Scanner(new File("data/Maps.txt"));
			// Delimit to read each line of the txt file
			inputFile.useDelimiter(",");

			// Read new lines in the csv file
			while (inputFile.hasNext()) {
				// Store each piece of information as properties of a TileMap object
				String type = inputFile.next();
				type = type.replace("\n", "");
				boolean[][] mapArray = new boolean[3][3];

				// Fill in boolean array
				for (int row = 0; row < mapArray.length; row++) {
					for (int col = 0; col < mapArray[row].length; col++) {
						mapArray[row][col] = inputFile.nextBoolean();
					}
				}

				// Add object to array
				maps.add(new TileMap(type, mapArray));
			}
			// Close file
			inputFile.close();

		} catch (FileNotFoundException e) {
			// Print if txt file not found
			System.out.println("File error");
		}
	}

	// Create grid to track player movement
	private void pathTracking() {
		// Fill path tracking assuming that player has not moved anywhere
		for (int i = 0; i < path.length; i++) {
			for (int j = 0; j < path[i].length; j++) {
				path[i][j] = '.';
			}
		}
	}

	public boolean traverseBoard(int row, int col) {
		// Increment number of moves
		numMoves++;

		// If no solution has been found
		if (row == getX() && col == getY() && numMoves > 1) {
			// Too many dead ends trap user back to their starting point
			return false;

			// Check if solution has been found
		} else if (reachedDestination(row, col)) {
			return true;
		} else {
			// If next move is not valid
			if (nextMove(row, col) == false) {
				// Can not move between adjacent tiles
				return false;
			}
			// If next move is valid
			return true;

		}
	}

	// Check if can move between adjacent tiles
	private boolean nextMove(int row, int col) {

		// Tracks change in movement
		int dRow = 0;
		int dCol = 0;

		// Use ranking system to determine which direction
		// the player will move in
		for (int count = 0; count < 4; count++) {
			switch (count) {

			case UP:
				dRow = -1;
				dCol = 0;
				break;

			case RIGHT:
				dRow = 0;
				dCol = 1;
				break;

			case DOWN:
				dRow = 1;
				dCol = 0;
				break;

			case LEFT:
				dRow = 0;
				dCol = -1;
			}

			// Check if direction player moved in/new tile is a valid move
			if (validMove(count, row, col, row + dRow, col + dCol)) {
				// If player arrived at destination or moved to next tile successfully
				// Continue to move in current direction
				// Do not backtrack
				if (traverseBoard(row + dRow, col + dCol)) {
					return true;
				}
			}
		}

		// Only mark as a dead end once all four adjacent tiles have been validated
		path[row][col] = '0';
		// Player must backtrack to PREVIOUS move (not the start)
		return false;
	}

	// Break up each tile into a 3x3 grid
	private boolean validMove(int direction, int currRow, int currCol, int nextRow, int nextCol) {
		// Get map of tile player is currently on
		TileMap currMap = getCurrMap(currRow, currCol);
		// Check if attempts to move off grid
		if ((nextRow < 0) || (nextRow > 6) || (nextCol < 0) || (nextCol > 6)) {
			return false;
		}

		else {
			// Get map of tile adjacent to player's current position
			TileMap adjacentMap = getAdjacentMap(nextRow, nextCol);

			// Ensure that player is moving in a new direction
			if (path[nextRow][nextCol] == '.') {
				// Determine which sides of tile are touching and if it forms a walkable path
				for (int i = 0; i < 3; i++) {

					if (direction == DOWN) {
						// Compare bottom row of current tile to top row of adjacent tile
						if ((currMap.getMap()[2][i]) && adjacentMap.getMap()[0][i]) {
							path[nextRow][nextCol] = 'X';
							return true;
						}
					} else if (direction == UP) {
						// Compare top row of current tile to bottom row of adjacent tile
						if ((adjacentMap.getMap()[2][i] && currMap.getMap()[0][i])) {
							path[nextRow][nextCol] = 'X';
							return true;
						}
					} else if (direction == RIGHT) {
						// Compare rightmost column of current tile to leftmost column of adjacent tile
						if ((currMap.getMap()[i][2]) && adjacentMap.getMap()[i][0]) {
							path[nextRow][nextCol] = 'X';
							return true;
						}
					}
					// If moving to the left
					else {
						// Compare leftmost column of current tile to rightmost column of adjacent tile
						if ((adjacentMap.getMap()[i][2] && currMap.getMap()[i][0])) {
							path[nextRow][nextCol] = 'X';
							return true;
						}
					}
				}
			}
		}

		// This specific adjacent tile does not form a walkable path
		return false;
	}

	// Reached selected tile
	private boolean reachedDestination(int currRow, int currCol) {

		if (currRow == destinationRow && currCol == destinationCol) {
			return true;
		} else {
			return false;
		}
	}

	// Determine walkable path of current tile
	private TileMap getCurrMap(int currRow, int currCol) {
		// Determine the type of tile
		String currType = getGrid()[currRow][currCol].getShape() + getGrid()[currRow][currCol].getRotation();

		// Get the current tile type
		for (int i = 0; i < maps.size(); i++) {
			if (maps.get(i).getName().equals(currType))
				return maps.get(i);
		}

		return null;
	}

	// Determine walkable path of adjacent tile
	private TileMap getAdjacentMap(int nextRow, int nextCol) {
		// Determine the type of tile
		String destinationType = getGrid()[nextRow][nextCol].getShape() + getGrid()[nextRow][nextCol].getRotation();

		// Get the current tile type
		for (int i = 0; i < maps.size(); i++) {
			if (maps.get(i).getName().equals(destinationType))
				return maps.get(i);
		}

		return null;
	}

	// Getters and setters
	public Tile[][] getGrid() {
		return grid;
	}

	public void setGrid(Tile[][] grid) {
		this.grid = grid;
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

	public int getNumMoves() {
		return numMoves;
	}

	public void setNumMoves(int numMoves) {
		this.numMoves = numMoves;
	}

	public ArrayList<TileMap> getMaps() {
		return maps;
	}

	public void setMaps(ArrayList<TileMap> maps) {
		this.maps = maps;
	}

	public int getDestinationRow() {
		return destinationRow;
	}

	public void setDestinationRow(int destinationRow) {
		this.destinationRow = destinationRow;
	}

	public int getDestinationCol() {
		return destinationCol;
	}

	public void setDestinationCol(int destinationCol) {
		this.destinationCol = destinationCol;
	}

	public boolean isFindingValidPaths() {
		return findingValidPaths;
	}

	public void setFindingValidPaths(boolean findingValidPaths) {
		this.findingValidPaths = findingValidPaths;
	}

	public static int getValidPaths() {
		return validPaths;
	}

	public static void setValidPaths(int validPaths) {
		AIMoveController.validPaths = validPaths;
	}

	public static int getUp() {
		return UP;
	}

	public static int getRight() {
		return RIGHT;
	}

	public static int getDown() {
		return DOWN;
	}

	public static int getLeft() {
		return LEFT;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public char[][] getPath() {
		return path;
	}

	public void setPath(char[][] path) {
		this.path = path;
	}

}
