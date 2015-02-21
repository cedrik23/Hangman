package com.quandidate.hangman.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quandidate.hangman.exceptions.NoMoreGameMovesException;
import com.quandidate.hangman.wordrepository.FileWordRepository;

/**
 * @author Amir
 * instance of a hangman game
 */
public class HangmanGameInstance implements Serializable{
	
	@Override
	public String toString() {
		return "HangmanGameInstance [id=" + id + ", completeWord="
				+ completeWord + ", word=" + word + ", triesLeft=" + triesLeft
				+ "]";
	}

	private static final Logger logger = LoggerFactory.getLogger(HangmanGameInstance.class);

	private static final int MAX_WORD_LENGTH = 2;

	private static final char MISSING_LETTER_CHAR = '.';

	private static final long serialVersionUID = -6809776717346275031L;
	
	/**
	 * gets the id of the game instance
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * sets the id of the instance
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * get the complete word for this game
	 * @return the complete word
	 */
	@JsonIgnore
	public String getCompleteWord() {
		return completeWord;
	}

	/**
	 * sets the complete word for this game
	 * @param completeWord
	 */
	@JsonIgnore
	public void setCompleteWord(String completeWord) {
		this.completeWord = completeWord;
	}

	/**
	 * get the game word
	 * @return
	 */
	public String getWord() {
		return word;
	}

	/**
	 * sets the game wor
	 * @param word
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * get the amount of tries left
	 * @return
	 */
	public int getTriesLeft() {
		return triesLeft;
	}

	/**
	 * sets the amount of tries left
	 * @param triesLeft
	 */
	public void setTriesLeft(int triesLeft) {
		this.triesLeft = triesLeft;
	}

	/**
	 * get the status of the game
	 * @return
	 */
	public GameState getStatus() {
		
		if(word.indexOf(MISSING_LETTER_CHAR) >= 0){
			if(triesLeft == 0){
				return GameState.Fail;
			}
			else{
				return GameState.Busy;
			}
		}
		else{
			return GameState.Success;
		}
	}

	public HangmanGameInstance(String id, String completeWord, int triesLeft)	{
		
		this.id = id;
		this.completeWord = completeWord;
		this.triesLeft = triesLeft;
		
		this.word = generateWordForGame(completeWord);
		
		logger.info("Created new game instance " + this);
	}
	
	private String generateWordForGame(String completeWord) {
		
		logger.info("generating word for game based on word: " + completeWord);
		StringBuilder gameWord = new StringBuilder(completeWord);
		int wordLength = completeWord.length();
		
		// how many letters to remove from word;
		Random r = new Random();
		int lettersToRemove = r.nextInt(wordLength - MAX_WORD_LENGTH) + 1;
		for (int i = 0; i < lettersToRemove; i++) {
			
			int indexOfLetterToReplace = r.nextInt(wordLength);
			char removeChar = gameWord.charAt(indexOfLetterToReplace); 
			
			gameWord.setCharAt(indexOfLetterToReplace, MISSING_LETTER_CHAR);
			
			missingChars.add(new MissingCharsInWord(indexOfLetterToReplace, removeChar));
		}
		
		word = gameWord.toString();
		
		logger.info("game word created: " + word);
		return word;
	}
	
	/**
	 * conduct a game move
	 * @param c the char to conduct the move with
	 */
	public void makeGameMove(char c) {

		logger.info("making game move: " + c);
		if(triesLeft == 0 || this.getStatus() ==  GameState.Success){
			throw new NoMoreGameMovesException(id);
		}
		
		// check if char exists in missing chars
		if(missingChars.contains(c)){
			logger.info("game move is good, char found in missing chars");
			StringBuilder sb = new StringBuilder(word);
			
			MissingCharsInWord missingChar = missingChars.get(c);
			int indexOfMissingChar = missingChar.getIndex();
			sb.setCharAt(indexOfMissingChar, c);
			word = sb.toString();
			
			missingChars.remove(missingChar);
		}
		else{
			logger.info("game move is not good, char not found in missing chars");
			triesLeft--;
		}
	}

	private String id;
	
	private String completeWord;
	
	private String word;
	
	private int triesLeft;
	
	private MissingCharsInWordList missingChars = new MissingCharsInWordList();
	
	/**
	 * helper class to hold a list of missing chars in the word
	 * @author Amir
	 *
	 */
	class MissingCharsInWordList extends ArrayList<MissingCharsInWord>
	{
		private static final long serialVersionUID = 141995801483099774L;
		
		/**
		 * indicates weather the given char is contained in the list of missing chars
		 * @param c
		 * @return
		 */
		public boolean contains(char c)
		{
			boolean result = false;
			
			for (MissingCharsInWord charInWord  : this) {
				if(charInWord.getCharacter() == c){
					result = true; 
					break;				
				}
			}
			
			return result;
		}
		
		/**
		 * get the first apperiance of the char in the list of missing chars
		 * @param c
		 * @return
		 */
		public MissingCharsInWord get(char c)
		{
			MissingCharsInWord returnValue = null;
			
			for (MissingCharsInWord charInWord  : this) {
				if(charInWord.getCharacter() == c){
					returnValue = charInWord; 
					break;				
				}
			}
			
			return returnValue;	
		}
	}
	
	/**
	 * helper class to hold missing chars in the word and their index in the word
	 * @author Amir
	 *
	 */
	class MissingCharsInWord{
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + character;
			result = prime * result + index;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MissingCharsInWord other = (MissingCharsInWord) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (character != other.character)
				return false;
			if (index != other.index)
				return false;
			return true;
		}

		MissingCharsInWord(int index, char character)
		{
			this.index = index;
			this.character = character;
		}
		
		private int index;
		
		private char character;

		/**
		 * gets the index where the char can be found
		 * @return
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * sets the index where the char can be found
		 * @param index
		 */
		public void setIndex(int index) {
			this.index = index;
		}

		/**
		 * gets the character
		 * @return
		 */
		public char getCharacter() {
			return character;
		}

		/**
		 * sets the character
		 * @param character
		 */
		public void setCharacter(char character) {
			this.character = character;
		}

		private HangmanGameInstance getOuterType() {
			return HangmanGameInstance.this;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((completeWord == null) ? 0 : completeWord.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + triesLeft;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HangmanGameInstance other = (HangmanGameInstance) obj;
		if (completeWord == null) {
			if (other.completeWord != null)
				return false;
		} else if (!completeWord.equals(other.completeWord))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (triesLeft != other.triesLeft)
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

	
}
