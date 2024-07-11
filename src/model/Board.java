package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.ImageIcon;

import controller.LabyrinthController;
import controller.MoveController;
import view.BoardPanel;
import view.FreeTilePanel;
import view.PlayerPanel;

/*
 * Labyrinth Game
 * Board Class
 * Group 4
 * Samantha Mac, Kelvin Nguyen
 * April 23, 2024
 */

// This class tracks the tiles located in each spot on the board
public class Board {
	// Create array of tiles
	public static ArrayList<Tile> tiles = new ArrayList<Tile>();

	// Add variables for the insertTile Method.
	static int buttonGone;

	// Create the grid of tiles
	public static Tile[][] grid = new Tile[7][7];
	
	// Fields for where the tile cannot be reinserted
	public static int undoTileX;
	public static int undoTileY;

	// Constructor
	public Board() {
		super();
		createBoard();
	}

	// Setter and getters
	public Tile[][] getGrid() {
		return grid;
	}

	public void setGrid(Tile[][] grid) {
		this.grid = grid;
	}

	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(ArrayList<Tile> tiles) {
		this.tiles = tiles;
	}

	// KELVIN ADDED
	public static int getButtonClicked(int buttonClicked) {
		return buttonGone;
	}

	// use "deepToString" for two-dimensional array
	@Override
	public String toString() {
		return "Board [grid=" + Arrays.deepToString(grid) + "]";
	}

	// Method that places tiles on the grid
	public void createBoard() {
		// Generate tiles
		createTreasureTiles();
		createTiles();
		createCorners();

		// Shuffle tiles to randomize board
		Collections.shuffle(tiles);

		// Insert unmoveable tiles onto board
		insertStationary();
		// Insert remaining tiles onto the board
		insertTiles();
	}

	public void createTreasureTiles() {
		// If txt file exists/ is accessible
		try {
			// Read txt using Scanner
			Scanner inputFile = new Scanner(new File("data/Treasures.csv"));
			// Delimit to read each line of the txt file
			inputFile.useDelimiter(",");

			// Read new lines in the csv file
			while (inputFile.hasNext()) {
				// Store each piece of information as properties of a tile object
				String name = inputFile.next();
				name = name.replace("\n", "");
				String shape = inputFile.next();
				int rotation = inputFile.nextInt();
				boolean moveable = inputFile.nextBoolean();
				int x = inputFile.nextInt();
				int y = inputFile.nextInt();

				// Randomize rotation for items not on the board
				if (rotation == -1) {
					Random rand = new Random();
					rotation = rand.nextInt(0, 4);
				}

				// Create Tile (treasure) objects with properties gathered from CSV file
				tiles.add(new Tile(name, shape, rotation, moveable, x, y));
			}
			// Close file
			inputFile.close();

		} catch (FileNotFoundException e) {
			// Print if txt file not found
			System.out.println("File error");
		}

	}

	public void createTiles() {
		// Create random object
		Random rand = new Random();
		// Create array of possible shapes
		String[] shapes = { "I", "L" };

		// Fill remainder of tiles array with moveable tile objects
		for (int i = 0; i < 22; i++) {
			// Generate randomized properties
			int shape = rand.nextInt(0, 2);
			int rotation = rand.nextInt(0, 4);

			// Create Tile objects with randomized properties
			tiles.add(new Tile(shapes[shape], shapes[shape], rotation, true, -1, -1));
		}
	}

	// Manually create tiles for where the characters start
	public void createCorners() {
		tiles.add(new Tile("RedStart", "L", 1, false, 0, 0));
		tiles.add(new Tile("BlueStart", "L", 2, false, 0, 6));
		tiles.add(new Tile("YellowStart", "L", 0, false, 6, 0));
		tiles.add(new Tile("GreenStart", "L", 3, false, 6, 6));
	}

	public void insertStationary() {
		// Iterate through tile list
		for (int i = 0; i < tiles.size(); i++) {
			// Check if tile is not moveable
			if (!(tiles.get(i).isMoveable())) {
				// Remove tile from stack and add to board
				grid[tiles.get(i).getX()][tiles.get(i).getY()] = tiles.remove(i);
				i--;
			}
		}
	}

	public void insertTiles() {
		// Iterate through board and see where there are empty spaces
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				// Check if a tile is present
				if (grid[row][col] == null) {
					// Update tile coordinates
					tiles.get(tiles.size() - 1).setX(row);
					tiles.get(tiles.size() - 1).setY(col);
					// Remove tile from stack and add to board
					grid[row][col] = tiles.remove(tiles.size() - 1);
				}
			}
		}
	}

	public static Tile returnFreeTile() {
		// Return last tile in arrayList
		return tiles.get(0);
	}

	// Coded by Kelvin
	// This method runs when the user clicks the insertTilButtons
	public static void insertTile(int buttonClicked) {
		// Hide buttons
		BoardPanel.removeAllInsertTileButtons();
		// Which button was clicked
		getButtonClicked(buttonClicked);

		// If the user clicks a button that will shift a row
		if (buttonClicked == 3 || buttonClicked == 4 || buttonClicked == 5 || buttonClicked == 6 || buttonClicked == 7
				|| buttonClicked == 8) {

			// Shift the rows using a switch case
			switch (buttonClicked) {
			// If button 3 was clicked
			case 3:
				BoardPanel.hideInsertTileButton(4);
				buttonGone = 4;
				insertLeft(buttonClicked);
				undoTileX = 1;
				undoTileY = 6;
				break;
			// If button 4 was clicked
			case 4:
				BoardPanel.hideInsertTileButton(3);
				buttonGone = 3;
				insertRight(buttonClicked);
				undoTileX = 1;
				undoTileY = 0;
				break;
			// If button 5 was clicked
			case 5:
				BoardPanel.hideInsertTileButton(6);
				buttonGone = 6;
				insertLeft(buttonClicked);
				undoTileX = 3;
				undoTileY = 6;
				break;
			// If button 6 was clicked
			case 6:
				BoardPanel.hideInsertTileButton(5);
				buttonGone = 5;
				insertRight(buttonClicked);
				undoTileX = 3;
				undoTileY = 0;
				break;
			// If button 7 was clicked
			case 7:
				BoardPanel.hideInsertTileButton(8);
				buttonGone = 8;
				insertLeft(buttonClicked);
				undoTileX = 5;
				undoTileY = 6;
				break;
			// If button 8 was clicked
			case 8:
				BoardPanel.hideInsertTileButton(7);
				buttonGone = 7;
				insertRight(buttonClicked);
				undoTileX = 5;
				undoTileY = 0;
				break;
			default:
				break;
			}
		}

		// If the user clicks a button that will shift a column
		if (buttonClicked == 0 || buttonClicked == 1 || buttonClicked == 2 || buttonClicked == 9 || buttonClicked == 10
				|| buttonClicked == 11) {

			// Shift the columns using a switch case
			switch (buttonClicked) {
			// If button 0 was clicked
			case 0:
				// Call method to hide the button across from it to prevent from reversing the
				// inserted Tile
				BoardPanel.hideInsertTileButton(9);
				buttonGone = 9;
				// Call the insert method
				insertDown(1);
				undoTileX = 6;
				undoTileY = 1;
				break;
			// If button 1 was clicked
			case 1:
				BoardPanel.hideInsertTileButton(10);
				buttonGone = 10;
				insertDown(3);
				undoTileX = 6;
				undoTileY = 3;
				break;
			// If button 2 was clicked
			case 2:
				BoardPanel.hideInsertTileButton(11);
				buttonGone = 11;
				insertDown(5);
				undoTileX = 6;
				undoTileY = 5;
				break;
			// If button 9 was clicked
			case 9:
				BoardPanel.hideInsertTileButton(0);
				buttonGone = 0;
				insertUp(1);
				undoTileX = 0;
				undoTileY = 1;
				break;
			// If button 10 was clicked
			case 10:
				BoardPanel.hideInsertTileButton(1);
				buttonGone = 1;
				insertUp(3);
				undoTileX = 0;
				undoTileY = 3;
				break;
			// If button 11 was clicked
			case 11:
				BoardPanel.hideInsertTileButton(2);
				buttonGone = 2;
				insertUp(5);
				undoTileX = 0;
				undoTileY = 5;
				break;
			default:
				break;

			}
		}

	}

	// This will shift a row to the left
	public static void insertLeft(int row) {

		// Add the tile that will be pushed out to the tiles array
		tiles.add(grid[row - 2][6]);
		// Shift the tiles to left
		for (int i = 6; i > 0; i--) {
			grid[row - 2][i] = grid[row - 2][i - 1];
		}

		// Check where players are located
		for (int i = 0; i < LabyrinthController.players.size(); i++) {
			// CHeck if player is located on row/col that just shifted
			if (LabyrinthController.players.get(i).getX() == (row - 2)) {
				// If player is at the end of the board
				if (LabyrinthController.players.get(i).getY() == 6) {
					// Move player to opposite side of the row
					LabyrinthController.players.get(i).setY(0);
				} else {
					// Shift player to the left
					LabyrinthController.players.get(i).setY(LabyrinthController.players.get(i).getY() + 1);
				}
				BoardPanel.moveWithTile(i); // Update icon
			}
		}

		// Assign the most right tile to the free tile by calling the method
		grid[row - 2][0] = returnFreeTile();

		afterInsert();
	}

	// This will shift a row to the right
	public static void insertRight(int row) {

		// Add the tile that will be pushed out to the tiles array
		tiles.add(grid[row - 3][0]);

		// Shift the tiles to right
		for (int i = 0; i < 6; i++) {
			grid[row - 3][i] = grid[row - 3][i + 1];
		}

		// Check where players are located
		for (int i = 0; i < LabyrinthController.players.size(); i++) {
			// CHeck if player is located on row/col that just shifted
			if (LabyrinthController.players.get(i).getX() == (row - 3)) {
				// If player is at the end of the board
				if (LabyrinthController.players.get(i).getY() == 0) {
					// Move player to opposite side of the row
					LabyrinthController.players.get(i).setY(6);
				} else {
					// Shift player to the left
					LabyrinthController.players.get(i).setY(LabyrinthController.players.get(i).getY() - 1);
				}
				BoardPanel.moveWithTile(i); // Update icon
			}
		}

		// Assign the most left tile to the free tile by calling the method
		grid[row - 3][6] = returnFreeTile();

		afterInsert();
	}

	// This will shift a col downwards
	public static void insertDown(int col) {
		// Add the tile that will be pushed out to the tiles array
		tiles.add(grid[6][col]);

		// Move the tiles down
		for (int i = 6; i > 0; i--) {
			grid[i][col] = grid[i - 1][col];
		}

		// Check where players are located and update their positions
		for (int i = 0; i < LabyrinthController.players.size(); i++) {
			if (LabyrinthController.players.get(i).getY() == col) {
				if (LabyrinthController.players.get(i).getX() == 6) {
					LabyrinthController.players.get(i).setX(0);
				} else {
					LabyrinthController.players.get(i).setX(LabyrinthController.players.get(i).getX() + 1);
				}
				BoardPanel.moveWithTile(i); // Update icon
			}
		}

		// Assign the highest tile to the free tile by calling the method
		grid[0][col] = returnFreeTile();

		afterInsert();
	}

	// This will shift a col upwards
	public static void insertUp(int col) {
		// Add the tile that will be pushed out to the tiles array
		tiles.add(grid[0][col]);

		// Move the tiles up
		for (int i = 0; i < 6; i++) {
			grid[i][col] = grid[i + 1][col];
		}

		// Check where players are located and update their positions
		for (int i = 0; i < LabyrinthController.players.size(); i++) {
			if (LabyrinthController.players.get(i).getY() == col) {
				if (LabyrinthController.players.get(i).getX() == 0) {
					LabyrinthController.players.get(i).setX(6);
				} else {
					LabyrinthController.players.get(i).setX(LabyrinthController.players.get(i).getX() - 1);
				}
				BoardPanel.moveWithTile(i); // Update icon
			}
		}

		// Assign the lowest tile to the free tile by calling the method
		grid[6][col] = returnFreeTile();

		afterInsert();
	}

	// Update board images and FreeTile image after insertion
	public static void afterInsert() {
		// Check for new possible paths after inserting a tile
		PlayerPanel.testPaths();
		// Remove old FreeTile
		tiles.remove(0);

		// Update images of tiles on the board
		BoardPanel.resetImage();
		// Update path finder
		BoardPanel.hidePath();

		// Update FreeTile
		FreeTilePanel.tile = tiles.get(0);
		// Get image of new FreeTile image
		FreeTilePanel.freeTile.setIcon(new ImageIcon(FreeTilePanel.tile.getImageFilePath()));
	}
}
