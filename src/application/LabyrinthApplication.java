package application;
import view.HomeFrame;

/*
 * Names: Samantha Mac (33%), Emma Shi (33%), Kelvin Nguyen (33%)
 * 
 * Samantha Mac: 
 * LabyrinthController, MoveController, Board, Pathway Object, Player Object, TileMap, BoardPanel, EndGameFrame, Creating the board, Moving players, moving players on tile shifts
 * Highlight all possible paths, the longest path, and the direct path to each player's treasure
 * 
 * Emma Shi: 
 * Home Frame, SetUpFrame, FreeTilePanel, Player Panel, Tile Object, Rotate Tile Feature, Card Object, Move Object, Grid State Object, AI Move Controller
 * AI Player (determine the optimal move to execute - includes inserting the tile and the player's resulting position), Hard AI Player (attempted block path and prevent win methods)
 * 
 * Kelvin Nguyen: 
 * LabyrinthController, Board, Sound, Player Object, BoardPanel, GameFrame, HomeFrame, RulesFrame, Insert Tile Feature, Rule that stops user from clicking the opposite button on the next turn
 * Animate player movement
 * 
 * Course Code: ICS4U1
 * Teacher: Mr. Fernandes
 * 
 * Title: Amazing Labyrinth Board Game
 * Description: The Amazing Labyrinth is a dynamic 2-4 player board game where players navigate a shifting maze to collect treasures. 
 * Each player has 1-6 cards representing their unique treasures, which they must strategically pursue while inserting tiles to alter the maze.
 * Once a player has collected all their treasures, they win the game! 
 * Features: Maze Setup, Maze Traversal, Tile Insertion, Card Flips, Ending Turn, AI Logic, Highlighted Possible Paths, Sound Effects
 * Major Skills: OOP, Java Swing Components (JLabel, JButton, JRadioButton), Loops, Selection Statements, Recursion, 
 * Arrays, ArrayLists, Algorithms, Stack, Methods
 * Areas of Concern (detailed explanation in the class): AIPlayer
 */

// Application class to run the program
public class LabyrinthApplication {
	public static void main(String[] args) {
		// Create the Labryinth Conroller Object
		new HomeFrame();
	}
}
