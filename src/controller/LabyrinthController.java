package controller;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import model.Card;
import model.Player;
import model.Tile;
import view.GameFrame;
import view.SetUpFrame;

/*
 * Labyrinth Game
 * LabyrinthController Classes
 * Group 4
 * Kelvin Nguyen, Samantha Mac
 */

// This class initializes player and card objects
public class LabyrinthController {

	// Create Stack to store all cards
	public static Stack<Card> cards = new Stack<Card>();
	// Create dynamic List of players
	public static ArrayList<Player> players = new ArrayList<Player>();

	// Initialize fields
	public static int numCards;
	public static int humanPlayers;
	public static int numAI;
	public static int numPlayers; // FOR CURRENT IMPLEMENTATION WITHOUT THE AI

	// Create new game frame
	public static GameFrame game;
	
	public LabyrinthController() {

		// Console (FOR TESTING)
		System.out.println("Num regular players: " + humanPlayers);
		System.out.println("Num cards: " + numCards);
		System.out.println("Num AI: " + numAI);
		System.out.println("Total players: " + numPlayers);

		// Instantiate card stack
		createCards();
		// Shuffle stack
		Collections.shuffle(cards);

		// Create Player objects
		createPlayers(numPlayers, numCards);
		
		// Create Game Frame
		game = new GameFrame();
	}

	// Setters and getters
	public static Stack<Card> getCards() {
		return cards;
	}

	public static void setCards(Stack<Card> cards) {
		LabyrinthController.cards = cards;
	}

	public static void createCards() {
		// If txt file exists/ is accessible
		try {
			// Read txt using Scanner
			Scanner inputFile = new Scanner(new File("data/Cards.csv"));
			// Delimit to read each line of the txt file
			inputFile.useDelimiter(",");

			// Read new lines in the csv file
			while (inputFile.hasNext()) {
				// Store each piece of information as properties of a card object
				String name = inputFile.next().replace("\n", "");
				boolean matched = inputFile.nextBoolean();

				// Create Card objects with properties gathered from CSV file
				cards.add(new Card(name, matched));
			}

			// Shuffle cards
			Collections.shuffle(cards);

			// Close file
			inputFile.close();

		} catch (FileNotFoundException e) {
			// Print if txt file not found
			System.out.println("File error");
		}
	}

	public static void createPlayers(int numPlayers, int numCards) {
		// Iterate through each player
		for (int i = 0; i < numPlayers; i++) {
			// Create ArrayList of cards for each player
			Card[] playerCards = new Card[numCards];
			// Iterate through number of cards per player
			for (int c = 0; c < numCards; c++) {
				// Fill array with randomized cards from stack
				playerCards[c] = cards.pop();
			}
			// Create Player object
			if (i == 0) // Player one players first
				players.add(new Player(("Player" + i), playerCards, true, false, 0, 0));
			else if (i == 1)
				players.add(new Player(("Player" + i), playerCards, false, false, 0, 6));
			else if (i == 2)
				players.add(new Player(("Player" + i), playerCards, false, false, 6, 0));
			else
				players.add(new Player(("Player" + i), playerCards, false, false, 6, 6));
			
			// Iterate through the players
			// If the currentPlayer instanceof AI - create an AIPLayer
		}
	}
}