package model;

import java.util.Arrays;
/*
 * Labyrinth Game
 * Pathway
 * Group 4
 * Samantha Mac
 * April 22, 2024
 */


// This file creates a Pathway object to track all possible pathways for human players
public class Pathway {
	// Fields
	// 7 by 7 grid to track possible paths
	// to all 49 tiles
	private int[][] path = new int[7][7];
	private int pathLength;
	private boolean hasTreasure;
	private boolean isViable;
	
	// Constructor
	public Pathway(int[][] path, int pathLength, boolean hasTreasure, boolean isViable) {
		super();
		this.path = path;
		this.pathLength = pathLength;
		this.hasTreasure = hasTreasure;
		this.isViable = isViable;
	}

	// Setters and getters
	public int[][] getPath() {
		return path;
	}

	public void setPath(int[][] path) {
		this.path = path;
	}

	public int getPathLength() {
		return pathLength;
	}

	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}

	public boolean isHasTreasure() {
		return hasTreasure;
	}

	public void setTreasureAmount(boolean hasTreasure) {
		this.hasTreasure = hasTreasure;
	}

	public boolean isViable() {
		return isViable;
	}

	public void setViable(boolean isViable) {
		this.isViable = isViable;
	}

	@Override
	public String toString() {
		return "Pathway [path=" + Arrays.toString(path) + ", pathLength=" + pathLength + ", hasTreasure="
				+ hasTreasure + ", isViable=" + isViable + "]";
	}
}
