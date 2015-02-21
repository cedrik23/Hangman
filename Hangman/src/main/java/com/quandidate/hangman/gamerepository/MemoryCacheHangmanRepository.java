package com.quandidate.hangman.gamerepository;


import java.util.concurrent.ConcurrentHashMap;

import com.quandidate.hangman.entities.HangmanGameInstance;
import com.quandidate.hangman.entities.HangmanGamesList;
import com.quandidate.hangman.exceptions.GameNotFoundException;

public class MemoryCacheHangmanRepository implements IHangmanRepository {

	private final ConcurrentHashMap<String, HangmanGameInstance> cache;
	
	public MemoryCacheHangmanRepository()
	{
		cache = new ConcurrentHashMap<String, HangmanGameInstance>();
	}
	

	@Override
	public HangmanGamesList getAllGames() {
		
		HangmanGamesList list = new HangmanGamesList(cache.values());
		
		return list;
	}

	@Override
	public HangmanGameInstance getGameInstance(String id) {
		
		if(cache.containsKey(id))
		{
			return cache.get(id);
		}
		
		throw new GameNotFoundException(id);
	}

	@Override
	public void saveGame(String id, HangmanGameInstance gameInstance) {
		
		cache.put(id, gameInstance);
	}

}
