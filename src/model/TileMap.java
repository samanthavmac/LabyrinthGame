package model;

import java.util.Arrays;

/*
 * Labyrinth Game
 * TileMap
 * Group 4
 * Samantha Mac
 * April 22, 2024
 */


// This file creates a TileMap object which describes where users can walk on a tile
public class TileMap {
	private String name;
	private boolean[][] map;

	// Constructor
	public TileMap(String name, boolean[][] map) {
		super();
		this.name = name;
		this.map = map;
	}

	// Setters and getters
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean[][] getMap() {
		return map;
	}

	public void setMap(boolean[][] map) {
		this.map = map;
	}


	@Override
	public String toString() {
		return "TileMap [name=" + name + ", map=" + Arrays.toString(map) + "]";
	}
	
	
	
}
