package view;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Board;
import model.Tile;

/*
 * Labyrinth Game
 * Player Class
 * Group 4
 * Samantha Mac
 * April 22, 2024
 */

public class GameFrame extends JFrame {	
	public GameFrame() {
		// Create frame
		super("The aMAZEing Labyrinth"); // Frame title
		setSize(1920, 1080);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// This holds the game board and tile
		BoardPanel boardPanel = new BoardPanel();
		boardPanel.setBounds(50, 70, 772, 772);
		add(boardPanel);

		// This holds the free tile that the player can rotate
		FreeTilePanel freeTilePanel = new FreeTilePanel(Board.returnFreeTile());
		freeTilePanel.setBounds(850, 70, 550, 100);
		add(freeTilePanel);

		PlayerPanel playerPanel = new PlayerPanel();
		playerPanel.setBounds(850, 180, 552, 720);
        playerPanel.setBackground(new Color(0, 0, 0, 0));
        playerPanel.setOpaque(true);
		add(playerPanel);
		
        
		JLabel background = new JLabel(new ImageIcon("images/GameFrameBackground.png"));
		background.setBounds(0, 0, 1920, 1080);
		add(background);
		
		setVisible(true);
	}
}