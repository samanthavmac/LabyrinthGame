package view;

/*
 * Name: Emma Shi 
 * Home Frame 
 * Group 4
 * Date: April 22nd, 2024
 */

//Imports
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Sound;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// This is the Home Page that the user's will see when onboarding onto the game
public class HomeFrame extends JFrame {

	// Fields
	private JPanel homeScreenPanel = new JPanel();
	private JButton playButton;
	private JButton rulesButton;

	// Add sound
	private Sound sound = new Sound();

	// Constructor
	public HomeFrame() {
		playMusic(2);
		setUpPanel();
		initializeButtons();
		framesetUp();
	}

	// This method sets up the setup panel
	private void setUpPanel() {
		// Load the background image
		ImageIcon backgroundImage = new ImageIcon("images/HomeFrame.png");

		// Create a JLabel to hold the background image
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundLabel.setBounds(0, 0, 1400, 800);

		// Set layout of homeScreenPanel to null
		homeScreenPanel.setLayout(null);
		homeScreenPanel.setOpaque(false); // Make sure the panel is transparent
		homeScreenPanel
				.setPreferredSize(new Dimension(backgroundImage.getIconWidth(), backgroundImage.getIconHeight()));

		// Add the background label to the panel
		homeScreenPanel.add(backgroundLabel);
		homeScreenPanel.setVisible(true);
	}

	// Initialize the buttons
	private void initializeButtons() {
		// Direct the user to the play page if the button is clicked
		playButton = new JButton();// creating instance of the JButton
		playButton.setBounds(537, 314, 290, 120);// setting the bounds
		playButton.setOpaque(false); // Make the button transparent
		playButton.setContentAreaFilled(false); // Make the button content area transparent
		playButton.setBorderPainted(false); // Remove the button border
		playButton.setFocusable(false); // Disable the focusable property
		// implement the action listener for when clicked
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// close this screen
				dispose();
				new SetUpFrame();
				playSoundEffect(3); // Add sound

			}
		});

		// Direct the user to the play page if the button is clicked
		rulesButton = new JButton();// creating instance of the JButton
		rulesButton.setBounds(537, 444, 290, 120);// setting the bounds
		rulesButton.setOpaque(false); // Make the button transparent
		rulesButton.setContentAreaFilled(false); // Make the button content area transparent
		rulesButton.setBorderPainted(false); // Remove the button border
		rulesButton.setFocusable(false); // Disable the focusable property
//		// implement the action listener for when clicked
		rulesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// close this screen
				new RulesFrame();
				dispose();
			}
		});

		// Add the buttons to the panel
		homeScreenPanel.add(playButton);
		homeScreenPanel.add(rulesButton);
	}

	// This method sets up the frame
	private void framesetUp() {

		add(homeScreenPanel);
		// Set the size of the frame
		setSize(1400, 800);
		// Terminate the program when the user chooses to exit/quit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Don't allow the user to resize the screen
		setResizable(false);
		setVisible(true); // Make the JFrame visible

	}

	// Play the audio
	public void playSoundEffect(int i) {
		// Set the correct audio file
		sound.setFile(i);
		// play the audio
		sound.play();
	}

	public void playMusic(int i) {
		// Set the correct audio file
		sound.setFile(i);
		// play the audio
		sound.loop();
	}
}