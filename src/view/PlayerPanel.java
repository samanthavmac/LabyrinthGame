package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import controller.LabyrinthController;
import controller.MoveController;
import model.Board;
import model.Card;
import model.Pathway;
import model.Sound;

import javax.swing.*;

/*
 * Labyrinth Game
 * Player Panel
 * Group 4
 * Samantha Mac, Emma Shi
 * April 22, 2024
 */

// This class displays whose turn it is and players' cards
public class PlayerPanel extends JPanel {

	// Fields
	// JPanels
	public static JPanel controlPanel = new JPanel();
	public static JPanel pathPanel = new JPanel();
	public static JPanel playerPanel = new JPanel();
	private static JPanel[] playerPanelRow = new JPanel[LabyrinthController.numPlayers];
	// JLabels
	private JLabel playerIcons[] = new JLabel[LabyrinthController.numPlayers];
	private static JLabel playerCards[][];
	// JButtons
	private static JButton endTurnButton;
	// Player scores
	public static int[] playerScores = new int[LabyrinthController.numPlayers]; // Index represents player number, value represents player score
	// Player rankings
	public static int[] playerRanks = new int[LabyrinthController.numPlayers]; // Index represents rank, value represents player number
	// Track all paths to 49 tiles
	public static Pathway[][] paths = new Pathway[7][7];
	// Create an array list of treasure buttons
	private static JButton[] treasureButtons = new JButton[LabyrinthController.numCards];
	// Create an array of treasure coordinates
	private static int[][] treasureCoor = new int[LabyrinthController.numCards][2];
	// Add sound
	private static Sound sound = new Sound();

	// KELVIN ADDED
	private static int buttonGone = -1;
	private static int buttonClicked = 0;
	
	// Constructor method
	public PlayerPanel() {
		playerPanel.setOpaque(false);

		setLayout(null); // Set layout to null for manual positioning
		createControlRow(); // Create row to display current player and end turn
		createPathRow(); // Create row to manage display of possible paths
		createPlayerRows(); // Create rows for each player
		displayPlayerIcons();
		displayCards(); // Display cards
		initializeScores();
		initializeTreasureButtons();
		
		// Pre-determine possible paths
		BoardPanel.hidePath(); // Hide any current paths
		testPaths(); // See which paths are viable
		analyzePath(); // Determine number of treasures and lengths of each path
	}

	private void createControlRow() {
		// Add topmost panel
		controlPanel.setBackground(new Color(225, 191, 143));
		controlPanel.setBounds(0, 0, 550, 100);
		add(controlPanel);
		
		// Add label for current player
        ImageIcon currentTurn = new ImageIcon("images/CurrentTurnHeader.png");
        JLabel currentTurnHeader = new JLabel(currentTurn);
        currentTurnHeader.setBounds(100, 100, 173, 35);
 
		// Add icon of player whose turn it is
		JLabel currentTurnIcon = new JLabel(new ImageIcon("images/Player" + (MoveController.whoseTurn()+1) + ".png"));
		currentTurnIcon.setOpaque(false);
		
		// Button to end turn
        Icon etb = new ImageIcon("images/EndTurnButton.png");
        JButton endTurnButton = new JButton(etb);
        endTurnButton.setBounds(0, 0, 149, 65);
        endTurnButton.setBorder(BorderFactory.createEmptyBorder(-5,-5,-5,-5));
        
		// Button functionality
        endTurnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// Ensure that player has inserted a tile
				if (BoardPanel.insertedTile) {
					// Flip card if player is on a treasure item
					collectTreasure();
					// Change player's turn
					if (!isEnd()) {
						changeTurn();
						// Update icon
						currentTurnIcon.setIcon(new ImageIcon("images/Player" + (MoveController.whoseTurn()+1) + ".png"));
						// Prevent user from moving
						BoardPanel.canMove = false;
						// Reset flag
		                BoardPanel.insertedTile = false;
		                
						// Make buttons reappear
						BoardPanel.appearAllInsertTileButtons();
						buttonGone = Board.getButtonClicked(buttonClicked);
						removeIllegalButton(buttonGone);
						
						// Hide any visible paths
						BoardPanel.hidePath();
						
		                playSoundEffect(3); // Add sound
					}
				}
				else {
				    JOptionPane.showMessageDialog(null, "Please insert a tile.");

				}
			}
		});

		// Add components to panel
		controlPanel.add(currentTurnHeader);
		controlPanel.add(currentTurnIcon);
		controlPanel.add(endTurnButton);
	}
	
	private void createPathRow() {
		// Add panel
		pathPanel.setBounds(0, 100, 550, 80);
        pathPanel.setBackground(new Color(225, 191, 143));
		add(pathPanel);
		
		// Show all possible destination tiles
        Icon sapb = new ImageIcon("images/ShowAllPathsButton.png");
        JButton showAllPathsButton = new JButton(sapb);
        showAllPathsButton.setBounds(0, 0, 41, 48);
        showAllPathsButton.setBorder(BorderFactory.createEmptyBorder(-2,-2,-2,-2));
        
		// Button functionality
		showAllPathsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				playSoundEffect(3);
				testPaths(); // See which paths are viable
				analyzePath(); // Determine number of treasures and lengths of each path
				BoardPanel.displayPath(); // Show all paths
			}
		});
		
		// Hide any visible paths
        Icon hpb = new ImageIcon("images/HidePathsButton.png");
        JButton hidePathButton = new JButton(hpb);
        hidePathButton.setBounds(0, 0, 41, 48);
        hidePathButton.setBorder(BorderFactory.createEmptyBorder(-2,-2,-2,-2));
        
		// Button functionality
		hidePathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				BoardPanel.hidePath(); // Change visibility of highlighted paths
				playSoundEffect(3);
			}
		});
		
		// Show the longest path
        JButton showLongestPathButton = new JButton(new ImageIcon("images/LongestPathButton.png"));
        showLongestPathButton.setBounds(0, 0, 41, 48);
        showLongestPathButton.setBorder(BorderFactory.createEmptyBorder(-2,-2,-2,-2));
		// Button functionality
		showLongestPathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				playSoundEffect(3);
				BoardPanel.hidePath(); // Hide any current paths
				testPaths(); // See which paths are viable
				analyzePath(); // Determine number of treasures and lengths of each path
				
				// Initialize coordinates
				int bestRow = LabyrinthController.players.get(MoveController.whoseTurn()).getX();
				int bestCol = LabyrinthController.players.get(MoveController.whoseTurn()).getY();
				
				for (int i = 0; i < BoardPanel.board.grid.length; i++) {
					for (int j = 0; j < BoardPanel.board.grid[i].length; j++) {
						// Find pathway with best length 
						if (paths[i][j].getPathLength() > paths[bestRow][bestCol].getPathLength()) {
							// Update best coordinates to travel to
							bestRow = i;
							bestCol = j;
						}
					}
				}
			
				// Display the best path
				BoardPanel.displayBestPath(bestRow, bestCol);
			}
		});

		pathPanel.add(showAllPathsButton);
		pathPanel.add(hidePathButton);
		pathPanel.add(showLongestPathButton);

	}
	
	// This will display buttons that allow players to view the path to their treasure
	private void initializeTreasureButtons() {
		for (int i = 0; i < treasureButtons.length; i++) {
			final int buttonIndex = i;
			
			// Create buttons
	        Icon tb = new ImageIcon("images/TreasureButton" + i + ".png");
	        JButton treasureButton = new JButton(tb);
	        treasureButtons[i] = treasureButton;
	        treasureButton.setBounds(0, 0, 41, 48);
	        treasureButton.setBorder(BorderFactory.createEmptyBorder(-2,-2,-2,-2));
	        
			// Add action listener
			treasureButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					playSoundEffect(3);
					BoardPanel.hidePath(); // Hide any current paths
					// Display path leading to treasure
					BoardPanel.displayBestPath(treasureCoor[buttonIndex][0], treasureCoor[buttonIndex][1]);
				}
			});
			// Hide initially
			treasureButtons[i].setVisible(false);
			// Add to panel
			pathPanel.add(treasureButtons[i]);
		}
	}
	
	
	// Create panel rows for each player
	private void createPlayerRows() {
		for (int i = 0; i < LabyrinthController.numPlayers; i++) {
			playerPanelRow[i] = new JPanel(); // Create new row
			playerPanelRow[i].setLayout(null); // Set layout to null for manual positioning
			playerPanelRow[i].setBounds(0, 180 + i * 120, 550, 120); // Position
			playerPanelRow[i].setVisible(true);
	        playerPanelRow[i].setOpaque(false);
			add(playerPanelRow[i]);
		}
	}

	// This method displays the player's icons
	private void displayPlayerIcons() {
		for (int index = 0; index < playerIcons.length; index++) {
			playerIcons[index] = new JLabel(new ImageIcon("images/Player" + (index + 1) + ".png")); // Get image
			playerIcons[index].setOpaque(false);
			playerIcons[index].setBounds(10, 10, 66, 75);
			playerPanelRow[index].add(playerIcons[index]); // Add the player icon to the corresponding row
		}
	}

	// This method displays the player's cards
	private void displayCards() {
		// Initialize the playerCards array
		playerCards = new JLabel[LabyrinthController.numPlayers][LabyrinthController.numCards];

		// Iterate over each player
		for (int i = 0; i < LabyrinthController.numPlayers; i++) {
			// Iterate over each card for the current player
			for (int j = 0; j < LabyrinthController.numCards; j++) {
				// Create a JLabel for each card and set its icon using the card name
				playerCards[i][j] = new JLabel(new ImageIcon("images/Card" + LabyrinthController.players.get(i).getCards()[j].getName() + ".png"));
				// Set the bounds of the cardLabel
				playerCards[i][j].setBounds(90 + j * 77, 0, 76, 100);
				playerPanelRow[i].add(playerCards[i][j]); // Add the JLabel directly to the PlayerPanel
			}
		}
	}
	
	private void initializeScores() {
		// Start scores and ranks at 0
		for (int i = 0; i < playerScores.length; i++) {
			playerScores[i] = 0;
			playerRanks[i] = -1;
		}
	}

	// If user ends turn on a piece of treasure
	public void collectTreasure() {
		// Cycle through player's cards
		for (int i = 0; i < LabyrinthController.players.get(MoveController.whoseTurn()).getCards().length; i++) {
			// Get curr card
			Card card = LabyrinthController.players.get(MoveController.whoseTurn()).getCards()[i];
			// Check if tile matches a card that is not already flipped
			if (card.getName().equals(BoardPanel.board.getGrid()[LabyrinthController.players.get(MoveController.whoseTurn()).getX()][LabyrinthController.players.get(MoveController.whoseTurn()).getY()].getName()) && !(card.isFlipped())) {
				// Flip the card
				LabyrinthController.players.get(MoveController.whoseTurn()).getCards()[i].setFlipped(true);
				// Update player score
				playerScores[MoveController.whoseTurn()]++;
				// Update player ranks
				updateRanks();
				// Check if game is over
				isEnd();
				// Visually flip the card
				flipCard(MoveController.whoseTurn(), i);
				// Add sound effect
				playSoundEffect(1);
			}
		}
	}
	
	// Update player rankings
	private void updateRanks() {
		// If player has flipped all of their cards
		if (playerScores[MoveController.whoseTurn()] == LabyrinthController.numCards) {
			// Rank them to next available spot
			for (int i = 0; i < playerRanks.length; i++) {
				if (playerRanks[i] == -1) {
					JOptionPane.showMessageDialog(this, "Player " + (MoveController.whoseTurn()+1) + " has collected all of their treasure!",
							"Player Achievement", JOptionPane.PLAIN_MESSAGE);
					playerRanks[i] = MoveController.whoseTurn(); // Add player to rankings
					LabyrinthController.players.get(MoveController.whoseTurn()).setDoneGame(true); // End player's turn
					break;
				}
			}
		}
	}
	
	// Check if entire game is over
	private boolean isEnd() {
		// Create boolean to track end of game
		boolean isEnd = true;
		for (int i = 0; i < playerScores.length; i++) {
			// If players flipped all of their cards
			if (playerScores[i] != LabyrinthController.numCards) {
				isEnd = false;
			}
		}
		
		// If flag is true, end game
		if (isEnd) {
			LabyrinthController.game.dispose(); // End current game
			new EndGameFrame(); // Show ranking
		}
		
		// Return flag
		return isEnd;
	}
	
	// Visually flips a card
	public static void flipCard(int playerTurn, int cardNum) {
		playerCards[playerTurn][cardNum].setIcon(new ImageIcon ("images/CardBacking.png")); // Change image of card
	}
	
	public static void changeTurn() {
		// If this is the last player in array
		if (MoveController.whoseTurn() == LabyrinthController.players.size()-1) {
			if (!(LabyrinthController.players.get(0).isDoneGame())) {
				// End current players turn
				LabyrinthController.players.get(MoveController.whoseTurn()).setTurn(false);
				// Start next players turn
				LabyrinthController.players.get(0).setTurn(true);
			}
			else {
				for (int i = 1 ; i < LabyrinthController.players.size(); i++) {
					// Next player can not be in the game still
					if (!(LabyrinthController.players.get(i).isDoneGame())) {
						// End current players turn
						LabyrinthController.players.get(MoveController.whoseTurn()).setTurn(false);
						// Start next players turn
						LabyrinthController.players.get(i).setTurn(true);
						break;
					}
				}
			}
		}
		else {
			// Iterate through players after current players
			for (int i = MoveController.whoseTurn()+1 ; i < LabyrinthController.players.size(); i++) {
				// Next player can not be in the game still
				if (!(LabyrinthController.players.get(i).isDoneGame())) {
					// End current players turn
					LabyrinthController.players.get(MoveController.whoseTurn()).setTurn(false);
					// Start next players turn
					LabyrinthController.players.get(i).setTurn(true);
					break;
				}
			}
		}
		
		
		// Hide treasure buttons
		for (JButton button : treasureButtons) 
			button.setVisible(false);
		
		// Find new paths for next player
		testPaths();
		analyzePath();
	}
	
	public static void removeIllegalButton(int buttonGone) {
			
			switch (buttonGone) {
				// If button 3 was clicked
		        case 3:
		            BoardPanel.hideInsertTileButton(3);
		            break;
		        // If button 4 was clicked
		        case 4:
		            BoardPanel.hideInsertTileButton(4);
		            break;
		        // If button 5 was clicked
		        case 5:
		            BoardPanel.hideInsertTileButton(5);
		            break;
		        // If button 6 was clicked
		        case 6:
		            BoardPanel.hideInsertTileButton(6);
		            break;
		        // If button 7 was clicked
		        case 7:
		            BoardPanel.hideInsertTileButton(7);
		            break;
		        // If button 8 was clicked
		        case 8:
		            BoardPanel.hideInsertTileButton(8);
		            break;
	            // If button 0 was clicked
	            case 0:
	                BoardPanel.hideInsertTileButton(0);
	                break;
	            // If button 1 was clicked
	            case 1:
	                BoardPanel.hideInsertTileButton(1);
	                break;
	            // If button 2 was clicked
	            case 2:
	                BoardPanel.hideInsertTileButton(2);
	                break;
	            // If button 9 was clicked
	            case 9:
	                BoardPanel.hideInsertTileButton(9);
	                break;
	            // If button 10 was clicked
	            case 10:
	                BoardPanel.hideInsertTileButton(10);
	                break;
	            // If button 11 was clicked
	            case 11:
	                BoardPanel.hideInsertTileButton(11);
	                break;
		        default:
		            break;
		}
	}
	
	// Test all 49 tiles as possible destinations
	public static void testPaths() {
		// Check if a path of each tile is possible
		for (int i = 0; i < BoardPanel.board.grid.length; i++) {
			for (int j = 0; j < BoardPanel.board.grid[i].length; j++) {
				// Assume this tile is the destination tile
				MoveController currTest = new MoveController(i, j, true);				
				// Store the path player took to get to destination
				paths[i][j] = new Pathway(currTest.path, 0, false, currTest.isViablePath);
			}
		}
	}
	
	// Determine which path is the best
	public static void analyzePath() {
		// Hide treasure buttons while updating
		for (JButton button : treasureButtons) 
			button.setVisible(false);
		
		// Test all paths to 49 tiles
		for (int i = 0; i < BoardPanel.board.grid.length; i++) {
			for (int j = 0; j < BoardPanel.board.grid[i].length; j++) {
				final int x = i;
				final int y = j;

				// Ensure that this is a viable path
				if (paths[i][j].isViable()) {
					// Determine all possible paths that lead directly to treasure
					// Iterate through players cards
					for (int c = 0; c < LabyrinthController.players.get(MoveController.whoseTurn()).getCards().length; c++) {
						// Get current card
						Card card = LabyrinthController.players.get(MoveController.whoseTurn()).getCards()[c];
						// Check if tile matches a card that is not already flipped
						if (card.getName().equals(BoardPanel.board.getGrid()[i][j].getName()) && !(card.isFlipped())) {
							// Update coordinates of treasure
							treasureCoor[c][0] = i;
							treasureCoor[c][1] = j;
							// Show button if path is available
							treasureButtons[c].setVisible(true);
						}
					}
					
					// Iterate through each tile in each pathway to determine pathway with longest
					for (int row = 0; row < 7; row++) {
						for (int col = 0; col < 7; col++) {
							if ((paths[i][j].getPath()[row][col] != -1) && (paths[i][j].getPath()[row][col] != 0)) {
								// Find longest path (if there are no viable paths with treasure)
								// Increment path length
								paths[i][j].setPathLength(paths[i][j].getPathLength()+1);
							}
						}
					}
				}
			}
		}
	}
	
	public static void changeTreasureButton(int i, int row, int col) {
		// Button functionality
		treasureButtons[i].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				playSoundEffect(3);
				System.out.println("Testing button" + i + "," + row + "," + col);
				BoardPanel.hidePath(); // Hide any current paths
				// Display path leading to treasure
				BoardPanel.displayBestPath(row, col);
			}
		});
	}
	
	// Play the audio
	public static void playSoundEffect(int i) {
		// Set the correct audio file
		sound.setFile(i);
		// play the audio
		sound.play();
	}
}