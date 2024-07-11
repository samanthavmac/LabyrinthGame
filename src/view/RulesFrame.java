package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Labyrinth Game
 * Board Panel Class
 * Group 4
 * Kelvin Nguyen
 * May 6, 2024
 */

// This class displays the rules of the game
public class RulesFrame extends JFrame {
	// Panel to display the background
	private JPanel rulesPanel = new JPanel();
	// Button to return to home
	private JButton homeButton = new JButton();
	
	public RulesFrame() {
		setUpPanel();
		addHomeButton();
		framesetUp();
	}
	
	private void setUpPanel() {
		// Load the background image
		ImageIcon backgroundImage = new ImageIcon("images/RulesFrame.png");

		// Create a JLabel to hold the background image
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundLabel.setBounds(0, 0, 1400, 800);

		// Style panel
		rulesPanel.setLayout(null);
		rulesPanel.setOpaque(false); // Make sure the panel is transparent
		rulesPanel.setPreferredSize(new Dimension(backgroundImage.getIconWidth(), backgroundImage.getIconHeight()));

		// Add the background label to the panel
		rulesPanel.add(backgroundLabel);
		rulesPanel.setVisible(true);
	}

	private void addHomeButton() {
		// Create and make the start button transparent
		homeButton.setBounds(1223, 80, 140, 140);
		homeButton.setOpaque(false); // Make the button transparent
		homeButton.setContentAreaFilled(false); // Make the button content area transparent
		homeButton.setBorderPainted(false); // Remove the button border
		homeButton.setFocusable(false); // Disable the focusable property
		// Return to home screen
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dispose(); // Close screen
				new HomeFrame();
			}
		});
		// Add the button to the screen label
		rulesPanel.add(homeButton);

	}
	
	private void framesetUp() {
		add(rulesPanel);
		// Set the size of the frame
		setSize(1400, 800);
		// Terminate the program when the user chooses to exit/quit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Don't allow the user to resize the screen
		setResizable(false);
		setVisible(true); // Make the JFrame visible
	}
	
}
