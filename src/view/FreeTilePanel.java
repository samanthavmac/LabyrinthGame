package view;

/* Emma Shi 
 * Group 4
 * Date: Friday, April 26th, 2024
 * Free Tile Panel
 */

//Imports
import javax.swing.*;

import model.Board;
import model.Sound;
import model.Tile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//This class will create the Free Tile panel and its neighbouring buttons
public class FreeTilePanel extends JPanel {

	// Fields
	public static Tile tile; // instantiate Tile object
	
	// JLabels
	public static JLabel freeTile = new JLabel();
	
	// Add sound
	private Sound sound = new Sound();

	// Constructor method
	public FreeTilePanel(Tile tile) {
		this.tile = tile;
		setOpaque(false);
		setupUI();
	}

	// This method sets up the FreeTilePanel
	private void setupUI() {
		setLayout(new BorderLayout()); // REPLACE BORDERLAYOUT WITH THE BOARDFRAME when completed

		// Random layout for now, will replace with Figma UI
		JPanel freeTilePanel = new JPanel(); 
		freeTilePanel.setOpaque(false);

		freeTile.setBounds(1088, 85, 135, 135);
		freeTile.setIcon(new ImageIcon(
				"images/" + Board.returnFreeTile().getName() + Board.returnFreeTile().getRotation() + ".png"));
		

		// Create the buttons
        Icon cb = new ImageIcon("images/RotateButton1.png");
        JButton clockwiseButton = new JButton(cb);
        clockwiseButton.setBounds(0, 0, 45, 44);
        clockwiseButton.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        Icon ccb = new ImageIcon("images/RotateButton0.png");
        JButton counterClockwiseButton = new JButton(ccb);
        counterClockwiseButton.setBounds(0, 0, 45, 44);
        counterClockwiseButton.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		// Add the buttons to the panel
		freeTilePanel.add(counterClockwiseButton);
		freeTilePanel.add(freeTile);
		freeTilePanel.add(clockwiseButton);

		// Add the panel to the frame (temporarily using the border layout
		add(freeTilePanel, BorderLayout.NORTH);

		// Add action listeners to the buttons
		clockwiseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				playSoundEffect(3);
				tile.setClockwiseClicked(true); // Set flag for clockwise rotation
				tile.setRotation(tile.rotateClockwise()); // Update rotation
				// Update the freeTile icon with the rotated image
				freeTile.setIcon(new ImageIcon(tile.getImageFilePath()));
				// Reset the flag after rotation
				tile.setClockwiseClicked(false);
			}
		});

		counterClockwiseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				playSoundEffect(3);
				tile.setCounterClockwiseClicked(true); // Set flag for counter-clockwise rotation
				tile.setRotation(tile.rotateCounterClockwise()); // Update rotation
				// Update the freeTile icon with the rotated image
				freeTile.setIcon(new ImageIcon(tile.getImageFilePath()));
				// Reset the flag after rotation
				tile.setCounterClockwiseClicked(false);
			}
		});

	}
	
	//Play the audio
	public void playSoundEffect(int i) {
		// Set the correct audio file
		sound.setFile(i);
		// play the audio
		sound.play();
	}

}