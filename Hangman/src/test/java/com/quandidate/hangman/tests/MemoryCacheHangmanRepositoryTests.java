package com.quandidate.hangman.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.quandidate.hangman.entities.HangmanGameInstance;
import com.quandidate.hangman.entities.HangmanGamesList;
import com.quandidate.hangman.gamerepository.MemoryCacheHangmanRepository;

public class MemoryCacheHangmanRepositoryTests extends HangmanTestsBase{
	
	private MemoryCacheHangmanRepository repository = new MemoryCacheHangmanRepository();
	
	@Test
	public void persistAndRetreiveTest(){
		String gameId = "42";
		HangmanGameInstance gameInstance = new HangmanGameInstance(gameId, "word", 11);
		
		repository.saveGame(gameId, gameInstance);
		
		HangmanGameInstance retrievedGame = repository.getGameInstance(gameId);
		
		assertEquals(gameInstance, retrievedGame);
		
		HangmanGamesList allGames = repository.getAllGames();
		assertEquals(allGames.size(), 1);
		assertEquals(allGames.get(0), gameInstance);
	}

}
