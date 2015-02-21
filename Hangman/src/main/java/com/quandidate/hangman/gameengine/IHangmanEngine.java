package com.quandidate.hangman.gameengine;

import java.util.List;

import com.quandidate.hangman.entities.HangmanGameInstance;
import com.quandidate.hangman.entities.HangmanGamesList;

/**
 * @author Amir
 *	Engine for the hangman game
 */
public interface IHangmanEngine {

	/**
	 * Creates a new Hangman game 
	 * @return id of the new game
	 */
	public abstract String createNewGame();

	/**
	 * Returns all game instances 
	 * @return all the game instances
	 */
	public abstract HangmanGamesList getAllGameInstances();

	/**
	 * returns a game instance
	 * @param id of the hangman game instance
	 * @return the requested hangman game instance
	 */
	public abstract HangmanGameInstance getGameInstance(String id);

	/**
	 * Makes a game instance
	 * @param c the character used
	 */
	public abstract void makeGameMove(String id, char c);

}