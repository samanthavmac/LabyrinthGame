package view;

/*
 * Labyrinth Game
 * End Game Frame
 * Group 4
 * Samantha Mac
 * May 5th, 2024
 */

// Imports
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// This class sets up the End Game Frame
public class EndGameFrame extends JFrame {
	// Main panel
	private JPanel endGamePanel = new JPanel();
	// Declare the label that will go on the setup panel
	private JLabel endGameScreenLabel;
	// JButton to return to home
	private JButton homeButton = new JButton();

	// Constructor
	public EndGameFrame() {
		backgroundSetup();
		frameSetup();
		showRanks();
		addHomeButton();
	}

	// This method sets up the background of set up frame
	private void backgroundSetup() {
		// Create the image icon
		ImageIcon background = new ImageIcon("images/EndGameFrame.png");
		// Add the image to the label
		endGameScreenLabel = new JLabel(background);
		endGameScreenLabel.setLayout(null);

		// Add the endgame label to the panel
		endGamePanel.setPreferredSize(new Dimension(background.getIconWidth(), background.getIconHeight()));
		endGamePanel.add(endGameScreenLabel);
	}

	// Set up the frame
	private void frameSetup() {
		add(endGamePanel);
		// Terminate the program when closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set the frame to full screen size
		setSize(1400, 800);
		setResizable(false);

		// Make the frame visible
		setVisible(true);

	}

	// Display the ranking
	private void showRanks() {
		// Display number of rows based on number of players
		for (int i = 0; i < PlayerPanel.playerRanks.length; i++) {
			// Display icon of winning player
			JLabel playerIcon = new JLabel(new ImageIcon("images/Player" + (1 + PlayerPanel.playerRanks[i]) + ".png"));
			playerIcon.setBounds(400, 177 + 120 * i, 96, 96);
			// Display their rank
			JLabel rank = new JLabel("#" + (i + 1));
			rank.setFont(new Font("Arial", Font.BOLD, 20)); // Style font
			rank.setBounds(350, 177 + 120 * i, 96, 96);
			// Add components to panel
			playerIcon.setVisible(true);
			endGameScreenLabel.add(rank);
			endGameScreenLabel.add(playerIcon);

		}
	}

	// Add the home button onto the screen
	private void addHomeButton() {
		// Create and make the start button transparent
		homeButton.setBounds(838, 400, 344, 121);
		homeButton.setOpaque(false); // Make the button transparent
		homeButton.setContentAreaFilled(false); // Make the button content area transparent
		homeButton.setBorderPainted(false); // Remove the button border
		homeButton.setFocusable(false); // Disable the focusable property
		// Return to home screen
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dispose(); // Close screen
				System.exit(0); // End application
				new HomeFrame();
			}
		});
		// Add the button to the screen label
		endGameScreenLabel.add(homeButton);

	}
}