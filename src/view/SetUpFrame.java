package view;

/* Name: Emma Shi 
 * Setup Frame
 * Group 4
 * Date: April 26th, 2024
 */

//imports
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import controller.LabyrinthController;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;

// This class sets up the Set Up Frame
public class SetUpFrame extends JFrame implements ActionListener {

	// Create text area for user input
	private final JTextArea numCardsTA = new JTextArea();

	// Create radio buttons
	private final JRadioButton[][] typePlayerRB = new JRadioButton[4][3];

	// Create the start button
	private final JButton startButton = new JButton();

	// Create the setup panel
	private JPanel setUpPanel = new JPanel();
	// Declare the label that will go on the setup panel
	private JLabel setUpScreenLabel;

	// Fields
	private int minPlayerCounter = 0;

	// Constructor
	public SetUpFrame() {
		backgroundSetup();
		addRadioButtons();
		addTextArea();
		addButtons();
		frameSetup();
	}

	// Sets up the background of set up frame
	private void backgroundSetup() {
		// Create the image icon
		ImageIcon setUpScreen = new ImageIcon("images/SetUpFrame.png");
		// Add the image to the label
		setUpScreenLabel = new JLabel(setUpScreen);

		// Add the loginScreen label to the panel
		setUpPanel.add(setUpScreenLabel);

	}

	// Sets up the position of the radio buttons
	private void addRadioButtons() {
		// Iterate through the rows
		for (int i = 0; i < typePlayerRB.length; i++) {
			ButtonGroup buttonGroup = new ButtonGroup(); // Create a ButtonGroup for each row
			// Iterate through the cols
			for (int j = 0; j < typePlayerRB[i].length; j++) {
				// Initialize player button
				typePlayerRB[i][j] = new JRadioButton();
				// Add the buttons to the buttongroup
				buttonGroup.add(typePlayerRB[i][j]); // Add radio buttons to the ButtonGroup

				// Set the bounds of the radiobutton
				typePlayerRB[i][j].setBounds(390 + j * 260, 145 + i * 120, 30, 30);
				// Allow the radiobutton to execute actions if selected
				typePlayerRB[i][j].addActionListener(this);
				// Add the radiobutton to the label
				setUpScreenLabel.add(typePlayerRB[i][j]);
			}
		}
	}

	// Adds the text area for the user to enter the number of cards
	private void addTextArea() {
		// Create the text area for the user's username
		numCardsTA.setBounds(464, 667, 84, 31);// Set the bounds
		numCardsTA.setOpaque(false); // Set the text area background to be transparent
		Font font = new Font("Arial", Font.BOLD, 30); // Set the font size
		numCardsTA.setFont(font); // Set the text area to the above font
		setUpScreenLabel.add(numCardsTA);

	}

	// Adds the start button onto the label
	private void addButtons() {
		// Create and make the start button transparent
		startButton.setBounds(730, 600, 310, 134);
		startButton.setOpaque(false); // Make the button transparent
		startButton.setContentAreaFilled(false); // Make the button content area transparent
		startButton.setBorderPainted(false); // Remove the button border
		startButton.setFocusable(false); // Disable the focusable property
		startButton.addActionListener(this);
		// Add the button to the screen label
		setUpScreenLabel.add(startButton);

	}

	// Setup the current frame
	private void frameSetup() {
		add(setUpPanel);
		// Terminate the program when closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set the frame to full screen size
		setSize(1400, 800);
		setResizable(false);

		// Make the frame visible
		setVisible(true);

	}

	// Helper method to clear all radio button selections if more than 3
	private void clearRadioSelections() {
		for (JRadioButton[] col : typePlayerRB) {
			for (JRadioButton button : col) {
				button.setSelected(false);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == startButton) {
			// Validate the input in the text area
			String numCardsStr = numCardsTA.getText().trim();
			try {
				int numCardsInput = Integer.parseInt(numCardsStr);
				if (numCardsInput < 1 || numCardsInput > 6) {
					// Show error dialog if input is not between 1 and 6
					JOptionPane.showMessageDialog(this, "Please enter a value between 1 and 6 inclusive.",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return; // Exit actionPerformed method without proceeding further
				}
				// Assign the value to the variable in LabyrinthController
				LabyrinthController.numCards = numCardsInput;

				// Clear counters
				minPlayerCounter = 0;
				LabyrinthController.humanPlayers = 0;
				LabyrinthController.numAI = 0;
				// Increment fields in LabyrinthController fields based on selected radio
				// buttons
				for (int i = 0; i < typePlayerRB.length; i++) {
					for (int j = 0; j < typePlayerRB[i].length; j++) {
						if (typePlayerRB[i][j].isSelected()) {
							switch (j) {
							case 0:
								minPlayerCounter += 1;
								LabyrinthController.humanPlayers += 1;
								break;
							case 1:
								minPlayerCounter += 1;
								LabyrinthController.numAI += 1;
								break;

							}
						}
					}
				}

				// Generalize as a player since AI player is not implemented
				LabyrinthController.numPlayers = LabyrinthController.humanPlayers + LabyrinthController.numAI;

			} catch (NumberFormatException e) {
				// Show error dialog if input is not a valid integer
				JOptionPane.showMessageDialog(this, "Please enter a valid number of cards between 1 and 6 inclusive.",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				// Clear the text area after displaying error message
				numCardsTA.setText("");
				return; // Exit actionPerformed method without proceeding further
			}

			// Validate the minimum number of players
			if (minPlayerCounter < 2) {
				// Show error dialog if input is not a valid integer
				JOptionPane.showMessageDialog(this, "You need a minimum of 2 players. Please re-enter your selections.",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				clearRadioSelections();

			} else {
				// Proceed with creating LabyrinthController
				dispose();
				new LabyrinthController();
			}
		}
	}

}