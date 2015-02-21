package com.quandidate.hangman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.quandidate.hangman.entities.HangmanGameInstance;
import com.quandidate.hangman.entities.HangmanGamesList;
import com.quandidate.hangman.exceptions.InvalidGameInputException;
import com.quandidate.hangman.gameengine.HangmanGameEngine;
import com.quandidate.hangman.gameengine.IHangmanEngine;
import com.quandidate.hangman.gamerepository.IHangmanRepository;
import com.quandidate.hangman.utilities.ConfigurationContainer;
import com.quandidate.hangman.utilities.IConfigurationContainer;
import com.quandidate.hangman.wordrepository.IWordRepository;

public class HangmanGameEngineTests extends HangmanTestsBase {

	private IHangmanEngine engine;
	private IConfigurationContainer configContainer;
	private IHangmanRepository gameRepository;
	private IWordRepository wordRepository;
	
	@Override
	@Before
	public void setUp() {
		
		super.setUp();
		
		configContainer = Mockito.mock(IConfigurationContainer.class);
		gameRepository = Mockito.mock(IHangmanRepository.class);
		wordRepository = Mockito.mock(IWordRepository.class);
		
		Mockito.stub(configContainer.getValue("TriesLeft")).toReturn("11");
		
		engine = new HangmanGameEngine(configContainer, gameRepository, wordRepository);
		
	}
	
	
	private static final Pattern GUID_FORMAT = Pattern.compile("^(urn\\:uuid\\:)?\\p{XDigit}{8}-?\\p{XDigit}{4}-?\\p{XDigit}{4}-?\\p{XDigit}{4}-?\\p{XDigit}{12}$");

	
	@Test
	public void createNewGame_NewGame_SuccessReturnedGameId(){
		
		// Arrange
		String completeWord = "randomword";
		Mockito.when(wordRepository.getRandomWord()).thenReturn(completeWord);
		
		// Act
		String returnedGameId = engine.createNewGame();
		
		// Assert
		Mockito.verify(wordRepository).getRandomWord();
		Mockito.verify(gameRepository).saveGame(Matchers.matches(GUID_FORMAT.pattern()), Matchers.any(HangmanGameInstance.class));
		
		assertTrue(GUID_FORMAT.matcher(returnedGameId).matches());
	}
	
	@Test
	public void getAllGameInstances_GetAllGames_SuccessGamesReturned(){
		
		// Arrange
		Collection<HangmanGameInstance> gamesCollection = new ArrayList<HangmanGameInstance>();
		HangmanGamesList gamesList = new HangmanGamesList(gamesCollection);
		
		Mockito.when(gameRepository.getAllGames()).thenReturn(gamesList);
		
		// Act
		HangmanGamesList allGameInstances = engine.getAllGameInstances();
				
		// Assert
		assertEquals(allGameInstances.size(), 0);
	}
	
	//@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Test(expected=InvalidGameInputException.class)
	public void getGameInstance_nullGameId_InvalidGameInputReturned(){
	
		// Arrange
		String gameId = null;
		
		// Act
		engine.getGameInstance(gameId);
				
		// Assert
	
	}
	
	@Test(expected=InvalidGameInputException.class)
	public void getGameInstance_EmptyGameId_InvalidGameInputReturned(){
	
		// Arrange
		String gameId = "";
		
		// Act
		engine.getGameInstance(gameId);
				
		// Assert
	
	}
	
	@Test()
	public void getGameInstance_GetGameInstance_SuccessInstancerReturned(){
	
		// Arrange
		String gameId = "42";
		
		HangmanGameInstance gameInstance = new HangmanGameInstance(gameId, "word", 11);
		Mockito.when(gameRepository.getGameInstance(gameId)).thenReturn(gameInstance);
		
		// Act
		HangmanGameInstance returnedGameInstance = engine.getGameInstance(gameId);
				
		// Assert
		assertEquals(gameInstance, returnedGameInstance);
	
	}
	
	@Test(expected=InvalidGameInputException.class)
	public void makeGameMove_nullGameId_InvalidGameInputReturned(){
	
		// Arrange
		String gameId = null;
		
		char c = 'c';
		 
		// Act
		engine.makeGameMove(gameId, c);
				
		// Assert
	
	}
	
	@Test(expected=InvalidGameInputException.class)
	public void makeGameMove_EmptyGameId_InvalidGameInputReturned(){
	
		// Arrange
		String gameId = "";
		
		char c = 'c';
		
		// Act
		engine.makeGameMove(gameId, c);
				
		// Assert
	
	}
	
	@Test()
	public void makeGameMove_ValidGameMore_MadeMoveAndSavedGame(){
	
		// Arrange
		String gameId = "42";
		char c = 'c';
		
		HangmanGameInstance gameInstance = Mockito.mock(HangmanGameInstance.class);
		
		Mockito.when(gameRepository.getGameInstance(gameId)).thenReturn(gameInstance);
	
		
		// Act
		engine.makeGameMove(gameId, c);
				
		// Assert
		Mockito.verify(gameInstance).makeGameMove(c);
		Mockito.verify(gameRepository).getGameInstance(gameId);
		Mockito.verify(gameRepository).saveGame(gameId, gameInstance);
	
	}
	
	
	
	
	
	
}
