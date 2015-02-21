package com.quandidate.hangman;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.quandidate.hangman.entities.HangmanGameInstance;
import com.quandidate.hangman.entities.HangmanGamesList;
import com.quandidate.hangman.exceptions.HangmanBaseException;
import com.quandidate.hangman.exceptions.InvalidGameInputException;
import com.quandidate.hangman.gameengine.HangmanGameEngine;
import com.quandidate.hangman.gameengine.IHangmanEngine;
import com.quandidate.hangman.utilities.ConfigurationContainer;
import com.quandidate.hangman.utilities.IConfigurationContainer;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HangmanController {
	
	private static final String GAME_MOVE_REGEX = "char=[a-z]";

	private static final Logger logger = LoggerFactory.getLogger(HangmanController.class);
	
	private final IHangmanEngine engine;
	
	@Autowired
	public HangmanController(IHangmanEngine engine)
	{
		this.engine = engine;
	}
			
	@RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<?> createNewGame(UriComponentsBuilder uriBuilder) throws Exception {

		logger.info("Creating new game");
		HttpHeaders headers = new HttpHeaders();
		
		try {
		
			String id = engine.createNewGame();
			
			UriComponents uriComponents = uriBuilder.path("/games/{id}").buildAndExpand(id);
		    headers.setLocation(uriComponents.toUri());
		    logger.info("New game create: " + id);
		} 
		catch(HangmanBaseException hangmanBaseException) {
			logger.info("Hangman excption thrown: " + hangmanBaseException);
			throw hangmanBaseException;
		}
		catch (Exception e) {
			logger.error("createNewGame: exception caught!", e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/games", method = RequestMethod.GET)
    public ResponseEntity<?> getAllGames(UriComponentsBuilder uriBuilder) throws Exception {
		
		logger.info("Getting all games");
		HangmanGamesList games = null;
		try {
		
			games = engine.getAllGameInstances();
				
			logger.info("done getting all games: " + games);
		} 
		catch(HangmanBaseException hangmanBaseException) {
			logger.info("Hangman excption thrown: " + hangmanBaseException);
			throw hangmanBaseException;
		}
		catch (Exception e) {
			logger.error("getAllGames: exception caught!", e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<HangmanGamesList>(games, HttpStatus.ACCEPTED);
		
	}
	
	@RequestMapping(value = "/games/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getGame(UriComponentsBuilder uriBuilder, @PathVariable String id) throws Exception {
		
		logger.info("Get game with id: " + id);
		HangmanGameInstance game = null;
		try {
		
			game = engine.getGameInstance(id);
			logger.info("Found game: " + game);	
		} 
		catch(HangmanBaseException hangmanBaseException) {
			logger.info("Hangman excption thrown: " + hangmanBaseException);
			throw hangmanBaseException;
		}
		catch (Exception e) {
			logger.error("getGame: exception caught!", e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<HangmanGameInstance>(game, HttpStatus.ACCEPTED);
		
	}
	
	@RequestMapping(value = "/games/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> makeGameMove(UriComponentsBuilder uriBuilder, @PathVariable String id, @RequestBody String gameMoveBody) throws IOException {
		
		logger.info(String.format("making game move on game %s with value %s", id, gameMoveBody));
		HttpHeaders headers = new HttpHeaders();
		
		try {
		
			if(gameMoveBody.matches(GAME_MOVE_REGEX))
			{
				engine.makeGameMove(id, gameMoveBody.charAt(5));
			}
			else
			{
				throw new InvalidGameInputException(gameMoveBody);
			}
			
			UriComponents uriComponents = uriBuilder.path("/games/{id}").buildAndExpand(id);
		    headers.setLocation(uriComponents.toUri());
		} 
		catch(HangmanBaseException hangmanBaseException) {
			logger.info("Hangman excption thrown: " + hangmanBaseException);
			throw hangmanBaseException;
		}
		catch (Exception e) {
			logger.error("makeGameMove: exception caught!", e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Void>(headers, HttpStatus.ACCEPTED);
		
	}
	
}
