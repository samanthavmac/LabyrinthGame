package model;

/*
 * Name: Emma Shi 
 * Hard AI Player Class
 * Group 4
 * Date: May 6th, 2024
 * Major Skills: OOP, 2D Arrays, ArrayList, Getters and Setters, Selection Statements, For-loops, Inheritance, Algorithms
 * 
 * Areas of Improvement/Concerns: 
 * - Was unable to debug/test this code since it was not linked with the basic game.
 * 
 * Algorithm/Thought Process
 * Preventing Wins: 
 * 1. Create a priority system that keeps track of the number of cards that each player has
 * 2. If a player has 1 card left, they are the highest priority. 
 * 2.1 If there are multiple players with 1 card left, determine which player is the closest (shortest number of moves) from winning
 * 3. Execute a blocking move
 * Blocking/Obstructing Paths: 
 * 1. Run the AI player's tile insertion algorithm using the current game state
 * 2. If the player can reach a treasure tile that matches their card (returns true), obstruct their path by 
 * inserting a tile so that a path is no longer formed (returns false)
 * 3. If the AI is unable to block the path, execute a regular move that is in its best interest to find a reachable treasure
 */

// Imports
import java.util.ArrayList;
import controller.AIMoveController;
import controller.LabyrinthController;

// This AI Player class implements additional features such as blocking paths and preventing wins 
public class HardAIPlayer extends AIPlayer {

	// Fields
	private boolean pathBlocked;

	// Constructor method
	public HardAIPlayer(String name, Card[] cards, boolean isTurn, boolean isDoneGame, int x, int y) {
		super(name, cards, isTurn, isDoneGame, x, y);
		setLocalGrid(localGrid); // Set the localGrid using the setter method
	}

	// Utility methods that block paths and prevent wins in addition to the basic
	// features of the AI

	// Method to prevent a win if another player has one card remaining
	public void preventWin() {
		// Identify players with only one card left
		ArrayList<Player> playersWithOneCardLeft = findPlayersWithOneCardLeft();

		// For each player with one card left, calculate and execute blocking moves
		for (Player player : playersWithOneCardLeft) {
			ArrayList<Move> blockingMoves = calculateBlockingMoves(player);
			executeBlockingMoves(blockingMoves);
		}
	}

	// Method to find players with only one card left
	private ArrayList<Player> findPlayersWithOneCardLeft() {
		ArrayList<Player> playersWithOneCardLeft = new ArrayList<>();
		for (Player player : LabyrinthController.players) {
			int cardCount = 0;
			for (Card card : player.getCards()) {
				if (card != null) {
					cardCount++;
				}
			}
			if (cardCount == 1) {
				playersWithOneCardLeft.add(player);
			}
		}
		return playersWithOneCardLeft;
	}

	// Method to determine the moves the AI must make to block a player
	private ArrayList<Move> calculateBlockingMoves(Player player) {
		ArrayList<Move> blockingMoves = new ArrayList<>();

		// Create a deep copy of the localGrid
		Tile[][] copiedGrid = copyGrid(getLocalGrid());

		// Iterate over each potential tile placement position on the board
		for (int row = 0; row < copiedGrid.length; row++) {
			for (int col = 0; col < copiedGrid[row].length; col++) {
				// Iterate over possible rotations for the tile at this position
				for (int rotation = 0; rotation < 4; rotation++) {
					// Check if placing a tile with this rotation at this position would obstruct
					// the player's path
					if (obstructsPlayerPath(player, row, col, rotation, copiedGrid)) {
						// If so, add a Move object representing the placement of a blocking tile
						blockingMoves.add(new Move(row, col, rotation, player));
					}
				}
			}
		}

		return blockingMoves;
	}

	// Method to determine if placing a tile at a specific position and rotation
	// obstructs the player's path
	private boolean obstructsPlayerPath(Player player, int row, int col, int rotation, Tile[][] grid) {
		// Create a copy of the grid to avoid modifying the original grid directly
		Tile[][] tempGrid = copyGrid(grid);

		// Place a tile with the specified rotation at the specified position
		tempGrid[row][col].setRotation(rotation);

		// Create a new MoveController instance to calculate the player's path on the
		// grid
		AIMoveController moveController = new AIMoveController(row, col, false, player.getX(), player.getY(), tempGrid);

		// Check if the player's path is obstructed on the grid
		pathBlocked = !moveController.traverseBoard(player.getX(), player.getY());
		return pathBlocked;
	}

	// Method to execute blocking moves on the game board
	private void executeBlockingMoves(ArrayList<Move> blockingMoves) {

		// If the player's path is blocked by the move
		if (pathBlocked == true) {
			// If there are multiple blocking moves, execute the first one in the ArrayList
			insertOptimalMove(blockingMoves.get(0));
			executePlayerMove(blockingMoves.get(0));

		}

		// Otherwise, player cannot be blocked so AI will execute the move in its best
		// interest
		else {
			findOptimalMove();

		}

	}
}
