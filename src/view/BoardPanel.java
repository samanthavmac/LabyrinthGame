package view;

// Imports
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.LabyrinthController;
import controller.MoveController;
import model.Board;
import model.Coordinate;
import model.Sound;
import model.Tile;

/*
 * Labyrinth Game
 * Board Panel Class
 * Group 4
 * Kelvin Nguyen, Samantha Mac
 * April 22, 2024
 */

// This class creates a visual representation of the board
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
	// Fields
	// Array of JLabels to represent tiles
	public static JLabel[][] tileJLabelArray = new JLabel[7][7];
	// Array of JLabels to represent walkable paths
	public static JLabel[][] pathJLabelArray = new JLabel[7][7];
	// Buttons to insert tile
	public static JButton[] insertTileJButtonArray = new JButton[12]; 	
	// Array of player icons/JLabels
	public static JLabel[] playerJLabelArray = new JLabel[LabyrinthController.players.size()];
	// JLabel to hold all elements
	private static JLabel boardLabel = new JLabel(new ImageIcon("images/GameBoard.png"));

	// Create board object
	public static Board board = new Board();
	
	// Player can not move until they insert a tile
	public static boolean canMove = false;
	// Player can not change turns until they insert a tile
	public static boolean insertedTile = false;
	
	// Track coordinates player has moved in for each path
	private static ArrayList<Coordinate> tempCoor = new ArrayList<Coordinate>();
	
	// Sound player
	static Sound sound = new Sound();
	
	// Constructor
	public BoardPanel() {
		// Add players to starting position
		addPlayerIcon();

		// Display tiles on board
		displayTile();
		// Add buttons along perimeter of board
		addInsertTileButtons();
		
		// Style panel
		boardLabel.setBounds(0, 0, 772, 772);
		setBackground(new Color(75, 55, 99, 100));
		add(boardLabel);
		setLayout(null);

	}
	
	// Setters and getters
	public JLabel[][] getTileJButtonArray() {
		return tileJLabelArray;
	}

	public void setTileJButtonArray(JLabel[][] tileJLabelArray) {
		this.tileJLabelArray = tileJLabelArray;
	}

	public JButton[] getInsertTileJButtonArray() {
		return insertTileJButtonArray;
	}

	public void setInsertTileJButtonArray(JButton[] insertTileJButtonArray) {
		this.insertTileJButtonArray = insertTileJButtonArray;
	}

	public JLabel getBoardLabel() {
		return boardLabel;
	}

	public void setBoardLabel(JLabel boardLabel) {
		this.boardLabel = boardLabel;
	}

	// This method inputs randomized tiles onto the board
	public static void displayTile() {
		// Iterate through each spot in grid
		for (int row = 0; row < tileJLabelArray.length; row++) {
			for (int col = 0; col < tileJLabelArray[row].length; col++) {
				// Store coordinates of each button
				final int buttonIndexX = row;
				final int buttonIndexY = col;
				
				// Get image of tile
				String imagePath = "images/" + board.getGrid()[row][col].getName() + board.getGrid()[row][col].getRotation() + ".png";

				// Set background as imagePath
				ImageIcon tileImage = new ImageIcon(imagePath);
				tileJLabelArray[row][col] = new JLabel();
				tileJLabelArray[row][col].setIcon(tileImage);
				pathJLabelArray[row][col] = new JLabel();

				// Style label
				tileJLabelArray[row][col].setBackground(new Color(75, 55, 99, 100));
				tileJLabelArray[row][col].setOpaque(true);
				tileJLabelArray[row][col].setLayout(null);

				// Format JLabels in a grid pattern
				tileJLabelArray[row][col].setBounds(col * 96 + 50, row * 96 + 50, 96, 96);

				// Add action listener to detect when player wants to move
				tileJLabelArray[row][col].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent event) {
						if (canMove)
							// Create a new Move Controller for each turn
							new MoveController(buttonIndexX, buttonIndexY, false);	
						else
						    JOptionPane.showMessageDialog(null, "Please insert a tile.");

						hidePath(); // Hide current paths
						// Analyze new pathways
						PlayerPanel.testPaths();
						PlayerPanel.analyzePath();
					}
				});
				
				// Add button to board
				boardLabel.add(tileJLabelArray[row][col]);
			}

		}
	}
	
	// This method update tile images after shifts
	public static void resetImage() {
		// Iterate through each spot in grid
		for (int row = 0; row < tileJLabelArray.length; row++) {
			for (int col = 0; col < tileJLabelArray[row].length; col++) {
				// Update image of the tile
				String imagePath = "images/" + board.getGrid()[row][col].getName() + board.getGrid()[row][col].getRotation() + ".png";
				tileJLabelArray[row][col].setIcon(new ImageIcon(imagePath));
			}
		}
	}
	
	public void addInsertTileButtons() {
		// Store index of insertTileButton array
		int i = 0; 
		
		// Iterate through each spot in grid
		for (int row = 0; row < tileJLabelArray.length; row++) {
			for (int col = 0; col < tileJLabelArray[row].length; col++) {
				
				if (((row == 1 || row == 3 || row == 5 ) && (col == 0 || col == 6)) || ((col == 1 || col == 3 || col == 5 ) && (row == 0 || row == 6))) {
					// KELVIN ADDED
					// Create a buttonIndex that is final to capture the variable and pass into the insertTile method 
					// (Source: https://medium.com/@nagarjun_nagesh/scopes-and-capturing-variables-in-java-lambda-functions-301249fd3f9e#:~:text=Capturing%20Final%20or%20Effectively%20Final,value%20in%20the%20enclosing%20context.)
					final int buttonIndex = i;
					
					// Create a new button
					// Set bounds depending on which side the buttons are on
					if (row == 0) {
						insertTileJButtonArray[i] = new JButton((new ImageIcon("images/InsertTileButton0.png")));	
						insertTileJButtonArray[i].setBounds(col * 96 + 75, row * 96, 50, 50);
					}
					else if (col == 0) {
						insertTileJButtonArray[i] = new JButton((new ImageIcon("images/InsertTileButton1.png")));	
						insertTileJButtonArray[i].setBounds(col * 96, row * 96 + 75, 50, 50);
					}
					else if (col == 6) {
						insertTileJButtonArray[i] = new JButton((new ImageIcon("images/InsertTileButton3.png")));	
						insertTileJButtonArray[i].setBounds(col * 96 + 150, row * 96 + 75, 50, 50);
					}
					else {
						insertTileJButtonArray[i] = new JButton((new ImageIcon("images/InsertTileButton2.png")));	
						insertTileJButtonArray[i].setBounds(col * 96 + 75, row * 96 + 150, 50, 50);
					}

					 // KELVIN ADDED
                    ActionListener buttonListener = new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            Board.insertTile(buttonIndex);
                            playSoundEffect(0); // Add sound
                            canMove = true; // Allow user to move
                    		insertedTile = true; // Player has inserted tile
                    		// Find new paths for next player
                    		PlayerPanel.testPaths();
                    		PlayerPanel.analyzePath();
                        }
                    };
                        
    				// KELVIN ADDED
                    // Add the action listener to the JButtons
                    insertTileJButtonArray[i].addActionListener(buttonListener);  
                    
                    // 
					boardLabel.add(insertTileJButtonArray[i]);
					
					// increment button index
					i++;
				}
			}
		}
	}
	
	// This method places players on the board
	public void addPlayerIcon() {
		// Iterate through number of players
		for (int i = 0; i < LabyrinthController.players.size(); i++) {
			// Create JLabel for icon of each player
			playerJLabelArray[i] = new JLabel(new ImageIcon("images/Player" + (i+1) + ".png"));
			// Style JLabel
			playerJLabelArray[i].setVisible(true);
			playerJLabelArray[i].setBounds(LabyrinthController.players.get(i).getY() * 96 + 50, LabyrinthController.players.get(i).getX() * 96 + 50, 96, 96);
			// Add to location on the board
			boardLabel.add(playerJLabelArray[i]);
		}
	}
	
	// Update visual location of player
	public static void movePlayerIcon(int player, int currRow, int currCol) {
		// Iteratre through each step
		for (int i = 1; i <= 49; i++) {
			// Iterate through each tile
			for (int row = 0; row < MoveController.path.length; row++) {
				for (int col = 0; col < MoveController.path[row].length; col++) {
					// Check that step count matches position
					if (MoveController.path[row][col] == i) {
						// Store coordinates to move in
						tempCoor.add(new Coordinate(row, col));
					}
				}
			}
		}	
		// Start animating movement
		animateIcon(player, tempCoor.get(0).getX(), tempCoor.get(0).getY(), currRow, currCol);
	}
	
	// This method moves player tile by tile for animation purposes
	public static void animateIcon(int player, int currentX, int currentY, int destinationX, int destinationY) {
		// Base case
		if (currentX == destinationX && currentY == destinationY) {
            return; // Arrived at destination
        }
		// Remove coordinate that it moved to
		tempCoor.remove(0);
		// Get next position
		int nextX = tempCoor.get(0).getX();
		int nextY = tempCoor.get(0).getY();
		
		playerJLabelArray[player].setBounds(nextY*96+50, nextX*96+50, 96, 96); // move label
		playSoundEffect(4);
		
		// Recursive call
		Timer delayTimer = new Timer(500, new ActionListener() { // Delay movement
            @Override
            public void actionPerformed(ActionEvent e) {
        		animateIcon(player, nextX, nextY, destinationX, destinationY);
            }
        });
        delayTimer.setRepeats(false); // Only delay once per animation
        delayTimer.start();
	}
		
	// Update visual location of player
	public static void moveWithTile(int player) {
		hidePath(); // hide any visible paths
		// Update player position
		playerJLabelArray[player].setBounds(LabyrinthController.players.get(player).getY() * 96 + 50, LabyrinthController.players.get(player).getX() * 96 + 50, 96, 96);
		// Place player on top of all elements
		boardLabel.setComponentZOrder(playerJLabelArray[player], 1);
	}

	// Display valid paths for user to see
	public static void displayPath() {
		// Iterate through each POTENTIAL destination
		for (int i = 0; i < 7; i ++) {
			for (int j = 0; j < 7; j++) {
				// Iterate through each PATHWAY to get to potential destination
				for (int row = 0; row < 7; row++) {
					for (int col = 0; col < 7; col++) {
						// Ensure that selected tile is walkable
						if ((PlayerPanel.paths[i][j].getPath()[row][col] != -1) && (PlayerPanel.paths[i][j].getPath()[row][col] != 0)) {
							// Determine what type of tile should be displayed
							// Make path icon visible
							pathJLabelArray[row][col].setIcon(new ImageIcon("images/" + BoardPanel.board.grid[row][col].getShape() + BoardPanel.board.grid[row][col].getRotation() + "Path.png"));
							pathJLabelArray[row][col].setBounds(col* 96 + 50, row * 96 + 50,96,96); // Set position
							pathJLabelArray[row][col].setVisible(true); // Make visible
						}
						// Add JLabel to corresponding tile
						boardLabel.add(pathJLabelArray[row][col]);
						// Place path underneath player, but above board
						boardLabel.setComponentZOrder(pathJLabelArray[row][col], 0);
					}
				}
			}
		}
	}
	
	// This method displays paths to treasure and the longest path
	public static void displayBestPath(int bestRow, int bestCol) {
		// Iterate through each PATHWAY to get to potential destination
		for (int row = 0; row < 7; row++) {
			for (int col = 0; col < 7; col++) {
				// Ensure that selected tile is walkable
				if ((PlayerPanel.paths[bestRow][bestCol].getPath()[row][col] != -1) && (PlayerPanel.paths[bestRow][bestCol].getPath()[row][col] != 0)) {
					// Determine what type of tile should be displayed
					// Make path icon visible
					pathJLabelArray[row][col].setIcon(new ImageIcon("images/" + BoardPanel.board.grid[row][col].getShape() + BoardPanel.board.grid[row][col].getRotation() + "Path.png"));
					pathJLabelArray[row][col].setBounds(col* 96 + 50, row * 96 + 50,96,96); // Set position
					pathJLabelArray[row][col].setVisible(true); // Make visible
				}
				// Add JLabel to corresponding tile
				boardLabel.add(pathJLabelArray[row][col]);
				boardLabel.setComponentZOrder(pathJLabelArray[row][col], 0);
			}
		}
	}
	
	// This method hides any visible paths
	public static void hidePath() {
		// Iterate through tiles on the board
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				// Make JLabel not visible
				BoardPanel.pathJLabelArray[i][j].setVisible(false);
			}
		}
	}
	
	// ADDED BY KELVIN
	// Hide the JButton so the user cannot move the button in the same way but opposite direction
	public static void hideInsertTileButton(int buttonIndex) {
	    insertTileJButtonArray[buttonIndex].setVisible(false);
	}
	
	// Make the button reappear after the user has chosen a different button
	public static void appearInsertTileButton(int buttonGone) {
	    insertTileJButtonArray[buttonGone].setVisible(true);
	}

	// KELVIN ADDED
	public static void removeAllInsertTileButtons() {
		for (int i = 0; i < 12; i++)
		    insertTileJButtonArray[i].setVisible(false);
		
	}
	
	// KELVIN ADDED
	public static void appearAllInsertTileButtons() {
		for (int i = 0; i < 12; i++)
		    insertTileJButtonArray[i].setVisible(true);
		
	}
	
	// KELVIN ADDED
	public static void playSoundEffect(int i) {
		sound.setFile(i);
		sound.play();
	}
}
