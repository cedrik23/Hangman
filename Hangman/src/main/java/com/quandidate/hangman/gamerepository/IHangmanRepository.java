package com.quandidate.hangman.gamerepository;

import com.quandidate.hangman.entities.HangmanGameInstance;
import com.quandidate.hangman.entities.HangmanGamesList;


/**
 * @author Amir
 * Repository of hangman games
 */
public interface IHangmanRepository {

	/**
	 * Get all the hangman games from repository
	 * @return all games list
	 */
	HangmanGamesList getAllGames();
	
	/**
	 * Get a hangman game instance from repository
	 * @param id the game id
	 * @return the game instance
	 */
	HangmanGameInstance getGameInstance(String id);
	
	/**
	 * persist a hangman game instance
	 * @param id the is of the game instance
	 * @param gameInstance the game instance
	 */
	void saveGame(String id, HangmanGameInstance gameInstance);
	
}
