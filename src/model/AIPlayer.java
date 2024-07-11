package model;

//Imports
import controller.AIMoveController;
import controller.LabyrinthController;
import controller.MoveController;
import view.FreeTilePanel;
import view.PlayerPanel;
import java.util.ArrayList;

/*	
 * Name: Emma Shi 
 * AI Player Class
 * Group 4
 * Date: May 6th, 2024
 * Major Skills: OOP, 2D Arrays, ArrayList, Getters and Setters, Selection Statements, For-loops, Inheritance, Algorithms
 * 
 * Areas of Improvement/Concerns: 
 * - Ideally, if no moves result in a reachable treasure, instead of executing a random move, execute a move that is calculated and 
 * will not result in a potential path for another player to reach a treasure
 *
 * Algorithm/Thought Process for the AI Player:
 * 1. Create a local copy of the board so that all possible moves can be tested without altering the actual game state
 * 1.1 Iterate through the 12 possible places to insert the tile
 * 1.1.1 If a tile is inserted and it results in a treasure being reachable, go to that coordinate. 
 * 1.1.1.1 Verify that the treasure matches the AI player's card
 * 1.1.1.2 If verified, execute the move on the actual board and end turn.
 * 1.1.2 Otherwise, reset to the original copy of the board. 
 * 1.1.2.1 Rotate tile
 * 1.1.2.2 Repeat steps 1.1 - 1.1.2.1
 * 2. If all rotations are tried and none result in a reachable treasure, insert into a random position into the board. 
 * 
 */

// This class extends the Player class by creating an easy AI Player. 
// This AI Player determines the optimal move - consisting of where to insert the tile (and it's rotation) and where the player will end up 
public class AIPlayer extends Player {

	// Fields
	public Tile[][] localGrid; // creates a local copy of the board
	private Tile freeTile; // creates an instance of the free tile object

	// Fields for the AI to determine the best move to make
	private ArrayList<Move> possibleMoves = allPossibleMoves();
	public Move optimalMove = findOptimalMove();

	// Constructor
	public AIPlayer(String name, Card[] cards, boolean isTurn, boolean isDoneGame, int x, int y) {
		super(name, cards, isTurn, isDoneGame, x, y); // Inherit from the player class
	}

	// Utility methods for the AI Player

	// Update the local copy of the game board
	public void updateLocalGrid() {
		setLocalGrid(copyGrid(Board.grid));
	}

	// Update the free tile information
	public void updateFreeTile() {
		Tile tile = new Tile(FreeTilePanel.tile.getName(), FreeTilePanel.tile.getShape(),
				FreeTilePanel.tile.getRotation(), FreeTilePanel.tile.isMoveable(), FreeTilePanel.tile.getX(),
				FreeTilePanel.tile.getY());
		setFreeTile(tile);
	}

	// Create a copy of the game board
	public Tile[][] copyGrid(Tile[][] grid) {
		// Create a copy of the grid with the same properties as the current state of
		// the grid
		Tile[][] newGrid = new Tile[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				newGrid[i][j] = grid[i][j];
			}
		}

		return newGrid;
	}

	// Generate all possible moves for the AI - 48 total, 12 insertions x 4
	// rotations
	public ArrayList<Move> allPossibleMoves() {
		for (int i = 1; i <= 5; i += 2) {
			for (int rot = 0; rot < 4; rot++) {
				// Add a possible move to each for each of the four sides of the board
				possibleMoves.add(new Move(0, i, rot));
				possibleMoves.add(new Move(6, i, rot));
				possibleMoves.add(new Move(i, 0, rot));
				possibleMoves.add(new Move(i, 6, rot));
			}
		}

		// When an element is removed, the size of the ArrayList decreases, and the
		// indices of subsequent elements shift accordingly.
		// Store the initial size of the array
		int initialSizeOfMoves = possibleMoves.size();

		// Iterate through the size of the ArrayList and remove an invalid move that
		// reverse the previous player's move
		for (int j = initialSizeOfMoves - 1; j >= 0; j--) {
			Move move = possibleMoves.get(j);
			// Remove the 4 moves that will undo the previous tile insertion
			if (move.getRow() == Board.undoTileX && move.getCol() == Board.undoTileY) {
				possibleMoves.remove(j);
			}
		}

		// Return an updated ArrayList with all the possible moves
		return possibleMoves;
	}

	// Make a move on the game board and return the resulting grid state
	public GridState makeMove(Move move, Tile[][] originalGrid) {

		// Set the rotation of the free tile to match the rotation specified in the move
		getFreeTile().setRotation(move.getRotation());

		// Create a copy of the original grid to avoid modifying the original grid
		// directly
		Tile[][] grid = copyGrid(originalGrid);

		// Initialize with the current x and y coordinates of the AI's piece
		GridState gridState = new GridState(getX(), getY(), grid);

		// Check if the move is to the leftmost column (col 0)
		if (move.getCol() == 0) {
			// Shift tiles to the right in the specified row to make space for the free tile
			for (int col = grid.length - 1; col >= 1; col--) {
				grid[move.getRow()][col] = grid[move.getRow()][col - 1];
			}
			// Place the free tile at the leftmost position in the specified row
			grid[move.getRow()][0] = getFreeTile();
			// Adjusting the y-coordinate of the AI's piece after the move
			if (getX() == move.getRow()) {

				// Adjust the current y coordinate (column) of the free tile after the move
				// e.g. col 6 becomes col 5, col 5 becomes col 4, etc.
				// ** col 0 becomes col 6
				gridState.setCurrent_y((getY() - 1) % grid.length);
			}
		} else if (move.getCol() == 6) {
			// Shift tiles to the left for other columns
			for (int col = 0; col < grid.length - 1; col++) {
				grid[move.getRow()][col] = grid[move.getRow()][col + 1];
			}
			// Place the free tile at the rightmost position in the specified row
			grid[move.getRow()][grid.length - 1] = getFreeTile();
			if (getX() == move.getRow()) {
				// Adjusting the y-coordinate of the AI's piece after the move
				gridState.setCurrent_y((getY() + 1) % grid.length);
			}
		} else if (move.getRow() == 0) {
			// Shift tiles downwards for the top row
			for (int row = grid.length - 1; row >= 1; row--) {
				grid[row][move.getCol()] = grid[row - 1][move.getCol()];
			}
			// Place the free tile at the topmost position in the specified column
			grid[0][move.getCol()] = getFreeTile();
			if (getY() == move.getCol()) {
				// Adjusting the x-coordinate of the AI's piece after the move
				gridState.setCurrent_x((getX() + 1) % grid.length);
			}
		} else if (move.getRow() == 6) {
			// Shift tiles upwards for the bottom row
			for (int row = 0; row < grid.length - 1; row++) {
				grid[row][move.getCol()] = grid[row + 1][move.getCol()];
			}
			// Place the free tile at the bottommost position in the specified column
			grid[grid.length - 1][move.getCol()] = getFreeTile();
			if (getY() == move.getCol()) {
				// Adjusting the x-coordinate of the AI's piece after the move
				gridState.setCurrent_x((getX() - 1) % grid.length);
			}
		}

		// Return the resulting grid state
		return gridState;
	}

	// Check if the location of the treasure matches the location on the grid that
	// the player moves to
	public boolean foundTreasure(int x, int y, Tile[][] grid) {
		// Iterate through the cards
		for (Card card : getCards()) {
			if (card.getName().equals(grid[x][y].getName()) && !card.isFlipped()) {
				return true;
			}
		}

		// If treasure location does not match, return false
		return false;
	}

	// Find the optimal move for the AI based on the current game state
	public Move findOptimalMove() {

		// Call on the utility methods
		updateLocalGrid();
		updateFreeTile();

		// Iterate through each possible move stored in the list of possibleMoves
		for (Move optimalMove : possibleMoves) {
			// Create a grid state after making the current move on the board
			GridState post_move_grid_state = makeMove(optimalMove, Board.grid);

			// Loop through each row of the grid
			for (int i = 0; i < post_move_grid_state.getGrid().length; i++) {
				// Loop through each column of the grid
				for (int j = 0; j < post_move_grid_state.getGrid()[0].length; j++) {
					// Check if a treasure is found at the current position (i, j) on the grid
					if (foundTreasure(i, j, post_move_grid_state.getGrid())) {
						// Create an AIMoveController object with the found treasure position and the
						// current grid state
						AIMoveController aiMoveController = new AIMoveController(i, j, false,
								post_move_grid_state.getCurrent_x(), post_move_grid_state.getCurrent_y(),
								post_move_grid_state.getGrid());
						// Check if the move controller is successful in finding a path to the treasure
						if (aiMoveController.isSuccess()) {
							// If successful, set the final destination (final_x and final_y) for the move
							// to the treasure
							optimalMove.setFinal_x(i);
							optimalMove.setFinal_y(j);
							try {
								// Pause to "think" (0.4 seconds)
								Thread.sleep(400);
							} catch (InterruptedException error) {
								System.out.println("Error");
							}
							insertOptimalMove(optimalMove); // helper method to insert the optimal move into the game
															// board
							executePlayerMove(optimalMove);
							// Return the move with the found treasure position
							return optimalMove;
						}
					}
				}
			}

		}

		// If no optimal move exists, just insert it in the first position
		Move optimalMove = new Move(0, 1, 0);
		optimalMove.setFinal_x(0);
		optimalMove.setFinal_y(1);
		insertOptimalMove(optimalMove); // helper method to insert the optimal move
		executePlayerMove(optimalMove); // helper method to execute the player move and reflect it on the board
		return optimalMove;
	}

	// Calls on the methods in the board panel that are responsible for executing
	// the optimal move
	public static void insertOptimalMove(Move optimalMove) {
		// Use the corresponding insert tile methods
		// Top of the board
		if (optimalMove.getRow() == 0) {
			Board.insertUp(optimalMove.getCol());
		}
		// Left of the board
		else if (optimalMove.getCol() == 0) {
			Board.insertLeft(optimalMove.getRow());

		}
		// Right of the board
		else if (optimalMove.getRow() == 6) {
			Board.insertRight(optimalMove.getCol());

		}
		// Bottom of the board
		else if (optimalMove.getCol() == 6) {
			Board.insertDown(optimalMove.getRow());

		}

	}

	// Use the existing controllers to update the player's coordinates
	public static void executePlayerMove(Move move) {
		try {
			// Pause to "think" (0.4 seconds)
			Thread.sleep(400);
		} catch (InterruptedException error) {
			System.out.println("Error");
		}
		// Update the location of the player
		LabyrinthController.players.get(MoveController.whoseTurn()).setX(move.getFinal_x());
		LabyrinthController.players.get(MoveController.whoseTurn()).setX(move.getFinal_y());

		// Change the current turn
		PlayerPanel.changeTurn();

	}

	// Getters and setters
	public Tile[][] getLocalGrid() {
		return localGrid;
	}

	public void setLocalGrid(Tile[][] localGrid) {
		this.localGrid = localGrid;
	}

	public Tile getFreeTile() {
		return freeTile;
	}

	public void setFreeTile(Tile freeTile) {
		this.freeTile = freeTile;
	}

}
