package model;

/*
 * Labyrinth Game
 * Sound Class
 * Group 4
 * Kelvin Nguyen
 * May 1, 2024
 */

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	// Create a clip object
	Clip clip;
	
	// Create a Sound URL array
	URL soundURL[] = new URL[5];
	
	// Constructor Method
	public Sound() {
		// Put the sounds into the array from the sound package
		soundURL[0] = getClass().getResource("/sound/tileClick.wav");
		soundURL[1] = getClass().getResource("/sound/itemCollected.wav");
		soundURL[2] = getClass().getResource("/sound/backgroundMusic.wav");
		soundURL[3] = getClass().getResource("/sound/buttonClick.wav");
		soundURL[4] = getClass().getResource("/sound/walking.wav");
	}
	
	// Set the correct audio to play when a sound needs to be played
	public void setFile(int i) {
		
		// Use try and catch to set the correct audio to play when called
		try {
			
			AudioInputStream audioIS = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(audioIS);
			
		} catch(Exception e) {
			
		}
	}
	
	// Play the audio when the method is called
	public void play() {
		
		// Play the audio
		clip.start();
		
	}
	
	// Loop the audio when the method is called
	public void loop() {
		
		// Loop the audio
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	// Stop the audio when the method is called
	public void stop() {
		
		// Stop the audio
		clip.stop();
	}
}
	
