package com.quandidate.hangman.gameengine;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.quandidate.hangman.HangmanController;
import com.quandidate.hangman.entities.HangmanGameInstance;
import com.quandidate.hangman.entities.HangmanGamesList;
import com.quandidate.hangman.exceptions.InvalidGameInputException;
import com.quandidate.hangman.gamerepository.IHangmanRepository;
import com.quandidate.hangman.utilities.ConfigurationContainer;
import com.quandidate.hangman.utilities.IConfigurationContainer;
import com.quandidate.hangman.wordrepository.IWordRepository;

public class HangmanGameEngine implements IHangmanEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(HangmanGameEngine.class);

	private static final int DEFAULT_TRIES_LEFT = 11;
	
	private final IConfigurationContainer configContainer;
	
	private final IHangmanRepository gameRepository;
	
	private final IWordRepository wordRepository;
	
	private final int configuredTriesLeft;
	
	@Autowired
	public HangmanGameEngine(IConfigurationContainer configContainer, IHangmanRepository gameRepository, IWordRepository wordRepository)
	{
		logger.info("HangmanGameEngine started...vvvrum vvvrum...");
		this.configContainer = configContainer;
		this.gameRepository = gameRepository;
		this.wordRepository = wordRepository;
				
		configuredTriesLeft = getConfiguredTriesLeft();	
	}
	
	/* (non-Javadoc)
	 * @see com.quandidate.hangman.IHangmanEngine#createNewGame()
	 */
	@Override
	public String createNewGame()
	{
		String randomWord = wordRepository.getRandomWord();
		HangmanGameInstance newInstance = new HangmanGameInstance(UUID.randomUUID().toString(), randomWord, configuredTriesLeft);
		logger.info("Created new game, persisting game to repository: " + newInstance);
		gameRepository.saveGame(newInstance.getId(), newInstance);
		
		return newInstance.getId();
	}
	
	/* (non-Javadoc)
	 * @see com.quandidate.hangman.IHangmanEngine#getAllGameInstances()
	 */
	@Override
	public HangmanGamesList getAllGameInstances()
	{
		return gameRepository.getAllGames();
	}
	
	/* (non-Javadoc)
	 * @see com.quandidate.hangman.IHangmanEngine#getGameInstance(int)
	 */
	@Override
	public HangmanGameInstance getGameInstance(String id)
	{
		if(id == null || id.isEmpty()){
			throw new InvalidGameInputException(id);
		}
				
		return gameRepository.getGameInstance(id);
	}
	
	/* (non-Javadoc)
	 * @see com.quandidate.hangman.IHangmanEngine#makeGameMove(char)
	 */
	@Override
	public void makeGameMove(String id, char c)
	{
		if(id == null || id.isEmpty()){
			throw new InvalidGameInputException(id);
		}
		
		HangmanGameInstance gameInstance = gameRepository.getGameInstance(id);
		logger.info("found game in repository: " + gameInstance);
		logger.info("making game move: " + c);
		gameInstance.makeGameMove(c);
		
		logger.info("saving game after move: " + gameInstance);
		gameRepository.saveGame(id, gameInstance);
		
	}
	
	private int getConfiguredTriesLeft() {
		
		int triesLeft = DEFAULT_TRIES_LEFT;
		String triesLeftString = this.configContainer.getValue("TriesLeft");
		try {
				triesLeft = Integer.parseInt(triesLeftString);
		} catch (Exception e) {
			logger.error("Error trying to format TriesLeft configuration, using default " + DEFAULT_TRIES_LEFT, e);
			
		}
		
		logger.info("Tries left value will be: " + triesLeft);
		return triesLeft;
	}

}
