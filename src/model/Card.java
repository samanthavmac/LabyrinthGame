package model;

/* Emma Shi 
 * Group 4 - Amazing Labryinth 
 * Date: Monday, April 22nd, 2024
 */

// Creating the card object - concrete class
public class Card {

	// Fields
	private String name; // name of the card
	private boolean flipped;

	// Constructor method
	public Card(String name, boolean flipped) {
		this.name = name;
		this.flipped = flipped;
	}

	// Getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public boolean isFlipped() {
		return flipped;
	}

	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

	@Override
	public String toString() {
		return "Card [name=" + name + ", flipped=" + flipped + "]";
	}

	
}