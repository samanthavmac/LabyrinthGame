package controller;


import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import model.Board;
import model.Card;
import model.Pathway;
import model.Player;
import model.Tile;
import model.TileMap;
import view.BoardPanel;
import view.PlayerPanel;


/*
 * Labyrinth Game
 * Move Controller
 * Player Class
 * Group 4
 * Samantha Mac
 * April 22, 2024
 */

// This class validates all player movements and displays player movements
public class MoveController {
	// Track number of moves
	private int numMoves = 0;
	// Store maps/grids of all possible tiles
	public ArrayList<TileMap> maps = new ArrayList<TileMap>();
	
	// Tile user clicked
	private int destinationRow; // x-coordinate
	private int destinationCol; // y-coordinate
	private boolean findingValidPaths; // Is the computer trying to find valid paths

	// Initialize variables about priority of movements
	private static final int UP = 0;
	private static final int RIGHT = 1;
	private static final int DOWN = 2;
	private static final int LEFT = 3;

	public static int[][] path; // Use integer map to track movements and their order
	public static boolean isViablePath; // Flag to track if path is walkable
	public static int stepCount = 1; // Number of movements per path

	public MoveController(int destinationRow, int destinationCol, boolean findingValidPaths) {		
		// Initialize fields
		this.destinationRow = destinationRow;
		this.destinationCol = destinationCol;
		this.findingValidPaths = findingValidPaths;
		
		// Generate boolean maps of tiles
		generateMaps();
		// Create a character grid to track player's path
		path = new int[7][7];
		// Initialize integer array to track the path
		pathTracking();
		
		// Mark where the player starts
		stepCount = 1;
		path[LabyrinthController.players.get(whoseTurn()).getX()][LabyrinthController.players.get(whoseTurn()).getY()] = stepCount;
		
		// Increment the number of steps player takes
		stepCount++;
		// Find a solution to player's destination
		traverseBoard(LabyrinthController.players.get(whoseTurn()).getX(), LabyrinthController.players.get(whoseTurn()).getY());
	}
	
	public static int whoseTurn() { // Who is the current player
		int currPlayer = 0;
		// Get player position
		for (int i = 0; i <  LabyrinthController.players.size(); i++) {
			// See which player's turn it is
			if (LabyrinthController.players.get(i).isTurn()) {
				// Store player name
				currPlayer = i;
				break;
			}
		}
		
		return currPlayer; // return player number
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
				path[i][j] = 0; // 0 represents player has not moved
			}
		}
	}
	
	// This method moves players between adjacent tiles
    public boolean traverseBoard(int row, int col) {
    	// Increment number of moves
        numMoves++;

        // If no solution has been found
        if (row == LabyrinthController.players.get(whoseTurn()).getX() && col == LabyrinthController.players.get(whoseTurn()).getY() && numMoves > 1) {
            // Too many dead ends trap user back to their starting point
            return false;

        // Check if solution has been found
        }
        else if (reachedDestination(row, col)) {
            return true;
        } 
        else {
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
	private boolean nextMove(int row, int col){
		 
		// Tracks change in movement
		int dRow = 0;
		int dCol = 0;
		
		// Use ranking system to determine which direction
		// the player will move in
		for(int count = 0; count < 4; count++){
			switch(count) {
				
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
			if(validMove(count, row, col, row + dRow, col + dCol)) {
				// If player arrived at destination or moved to next tile successfully
				// Continue to move in current direction
				// Do not backtrack
				path[row+dRow][col+dCol] = stepCount;
				// Increment number of steps taken
		        stepCount++;
				if(traverseBoard(row + dRow, col + dCol)) {
					return true;
				}
			}
		}
		
		// Only mark as a dead end once all four adjacent tiles have been validated
        path[row][col] = -1;
		// Player must backtrack to PREVIOUS move (not the start)
		return false;
	}
	
	// Break up each tile into a 3x3 grid
	private boolean validMove(int direction, int currRow, int currCol, int nextRow, int nextCol){
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
			if (path[nextRow][nextCol] == 0) {
				// Determine which sides of tile are touching and if it forms a walkable path
				for (int i = 0; i < 3; i++) {
					
					if (direction == DOWN) {
						// Compare bottom row of current tile to top row of adjacent tile
						if ((currMap.getMap()[2][i]) && adjacentMap.getMap()[0][i]) {
							return true;
						}
					}
					else if (direction == UP) {
						// Compare top row of current tile to bottom row of adjacent tile
						if ((adjacentMap.getMap()[2][i] && currMap.getMap()[0][i])) {
						     return true;
						}
					}
					else if (direction == RIGHT) {
						// Compare rightmost column of current tile to leftmost column of adjacent tile
						if ((currMap.getMap()[i][2]) && adjacentMap.getMap()[i][0]) {
					        return true;
						}
					}
					// If moving to the left
					else {
						// Compare leftmost column of current tile to rightmost column of adjacent tile
						if ((adjacentMap.getMap()[i][2] && currMap.getMap()[i][0])) {
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
		// If coordinates match and player wants to move there
		if((currRow == destinationRow) && (currCol == destinationCol) && !findingValidPaths) {
			// Update players position
			LabyrinthController.players.get(whoseTurn()).setX(currRow);
	        LabyrinthController.players.get(whoseTurn()).setY(currCol);
	        // Move on the board
	        BoardPanel.movePlayerIcon(whoseTurn(), currRow, currCol);
	        // Has reached destination
			return true;

		}
		// If coordinates match and player wants to show path
		else if((currRow == destinationRow) && (currCol == destinationCol) && findingValidPaths) {
			// Set as viable path
			isViablePath = true;
	        // Has reached destination
			return true;
		}
		else {
			// Is not a viable path
			isViablePath = false;
			// Has not reached destination
			return false;
		}	
	}
	
	// Determine walkable path of current tile
	private TileMap getCurrMap(int currRow, int currCol) {
		// Determine the type of tile
		String currType = BoardPanel.board.getGrid()[currRow][currCol].getShape() + BoardPanel.board.getGrid()[currRow][currCol].getRotation();

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
		String destinationType = BoardPanel.board.getGrid()[nextRow][nextCol].getShape() + BoardPanel.board.getGrid()[nextRow][nextCol].getRotation();

		// Get the current tile type
		for (int i = 0; i < maps.size(); i++) {
			if (maps.get(i).getName().equals(destinationType))
				return maps.get(i);
		}
		
		return null;
	}
}
