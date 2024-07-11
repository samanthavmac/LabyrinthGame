package model;

/*
 * Labyrinth Game
 * Player Class
 * Group 4
 * Samantha Mac
 * April 22, 2024
 */

public class Player {
	// Fields
	private String name;
	private Card[] cards; // Depends on what number user enters
	boolean isTurn; // Players turn to move
	boolean isDoneGame; // Player done game
	private int x; // Current row player is located in
	private int y; // Current row column is located in
	
	// Constructor
	public Player(String name, Card[] cards, boolean isTurn, boolean isDoneGame, int x, int y) {
		super();
		this.name = name;
		this.cards = cards;
		this.isTurn = isTurn;
		this.isDoneGame = isDoneGame;
		this.x = x;
		this.y = y;
	}

	// Setters and getters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Card[] getCards() {
		return cards;
	}

	public void setCards(Card[] cards) {
		this.cards = cards;
	}


	public boolean isTurn() {
		return isTurn;
	}

	public void setTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}
	
	public boolean isDoneGame() {
		return isDoneGame;
	}

	public void setDoneGame(boolean isDoneGame) {
		this.isDoneGame = isDoneGame;
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", isTurn=" + isTurn + ", x=" + x + ", y=" + y + "]";
	}
}
