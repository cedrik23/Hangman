package com.quandidate.hangman.wordrepository;

/**
 * @author Amir
 * a word repository
 */
public interface IWordRepository {
	
	/**
	 * get a random word from the repository
	 * @return the random word
	 */
	public String getRandomWord();
}
