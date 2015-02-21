package com.quandidate.hangman.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.quandidate.hangman.entities.GameState;
import com.quandidate.hangman.entities.HangmanGameInstance;
import com.quandidate.hangman.exceptions.InvalidGameInputException;
import com.quandidate.hangman.exceptions.NoMoreGameMovesException;

public class HangmanGameInstanceTests extends HangmanTestsBase {

	@Test
	public void getStatus_TriesLeftIsZeroWithMissingChars_ReturnStatusFail() {
	
		// Arrange
		String id = "42";
		String completeWord = "word";
		int triesLeft = 0;
		
		HangmanGameInstance instance = new HangmanGameInstance(id, completeWord, triesLeft);
		
		// Act & Assert
		assertTrue(instance.getWord().contains("."));
		assertEquals(GameState.Fail, instance.getStatus());
		
	}
	
	@Test
	public void getStatus_TriesLeftAboveZeroWithMissingChars_ReturnStatusBusy() {
		// Arrange
		String id = "42";
		String completeWord = "word";
		int triesLeft = 3;
		
		HangmanGameInstance instance = new HangmanGameInstance(id, completeWord, triesLeft);

		// Act & Assert
		assertTrue(instance.getWord().contains("."));
		assertEquals(GameState.Busy, instance.getStatus());
	}
	
	@Test
	public void getStatus_TriesLeftAboveZeroWithoutMissingChars_ReturnStatusSuccess() {
		// Arrange
		String id = "42";
		String completeWord = "word";
		int triesLeft = 3;
		
		HangmanGameInstance instance = new HangmanGameInstance(id, completeWord, triesLeft);
		instance.setWord(completeWord);
		
		// Act & Assert
		assertEquals(GameState.Success, instance.getStatus());
	}
	
	@Test(expected=NoMoreGameMovesException.class)
	public void makeGameMove_NoMoreTriesLeft_NoMoreTriesLeftException() {
		// Arrange
		char c = 'c';
		String id = "42";
		String completeWord = "word"; 
		int triesLeft = 0;
		
		HangmanGameInstance instance = new HangmanGameInstance(id, completeWord, triesLeft);
		
		
		// Act
		instance.makeGameMove(c);
		
		// Assert
		
	}
	
	@Test
	public void makeGameMove_CharNotInWord_OneLessTry() {
		// Arrange
		char c = 'c';
		String id = "42";
		String completeWord = "word"; 
		int triesLeft = 3;
		
		HangmanGameInstance instance = new HangmanGameInstance(id, completeWord, triesLeft);
		
		
		// Act
		instance.makeGameMove(c);
		
		// Assert
		assertEquals(triesLeft - 1, instance.getTriesLeft());
		
	}
	
	@Test
	public void makeGameMove_CharNotInWordOneTryLeft_OneLessTryGameIsFail() {
		// Arrange
		char c = 'c';
		String id = "42";
		String completeWord = "word"; 
		int triesLeft = 1;
		
		HangmanGameInstance instance = new HangmanGameInstance(id, completeWord, triesLeft);
		
		
		// Act
		instance.makeGameMove(c);
		
		// Assert
		assertEquals(triesLeft - 1, instance.getTriesLeft());
		assertEquals(GameState.Fail, instance.getStatus());
		
	}
	
	@Test
	public void makeGameMove_CharInWord_TriesNotReducedCharIsPlacedInWord() {
		// Arrange
		String id = "42";
		String completeWord = "word"; 
		int triesLeft = 3;
		
		HangmanGameInstance instance = new HangmanGameInstance(id, completeWord, triesLeft);
		String word = instance.getWord();
		
		char c = completeWord.charAt(word.indexOf('.'));
		
		StringBuilder sb = new StringBuilder(word);
		sb.setCharAt(word.indexOf('.'), c);
		
		
		// Act
		instance.makeGameMove(c);
		
		// Assert
		assertEquals(triesLeft, instance.getTriesLeft());
		assertEquals(sb.toString(), instance.getWord());
		
	}


}
