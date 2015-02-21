package com.quandidate.hangman.tests;

import static org.junit.Assert.*;


import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.quandidate.hangman.HangmanController;
import com.quandidate.hangman.entities.HangmanGameInstance;
import com.quandidate.hangman.entities.HangmanGamesList;
import com.quandidate.hangman.exceptions.HangmanBaseException;
import com.quandidate.hangman.exceptions.InvalidGameInputException;
import com.quandidate.hangman.gameengine.HangmanGameEngine;
import com.quandidate.hangman.gameengine.IHangmanEngine;
import com.quandidate.hangman.utilities.ConfigurationContainer;
import com.quandidate.hangman.utilities.IConfigurationContainer;


public class ControllerTests extends HangmanTestsBase{

	private static final String URI_STRING = "/Hangmanstests";
	private IHangmanEngine engineMock;
	private HangmanController controller;

	@Before
	@Override
	public void setUp(){
		
		engineMock = Mockito.mock(IHangmanEngine.class);
		IConfigurationContainer configurationContainerMock = Mockito.mock(IConfigurationContainer.class);
		controller = new HangmanController(engineMock);
	}

	@Test
	public void createNewGame_CreateNewGame_Success() throws Exception
	{
		// Arrange
		HttpStatus expectedStatus = HttpStatus.CREATED;
		String uriString = URI_STRING;
		String expectedGameId = "42";
		String expectedLocation = uriString + "/games/" + expectedGameId;
		
		Mockito.when(engineMock.createNewGame()).thenReturn(expectedGameId);
		
		// Act
		
		ResponseEntity<?> responseEntity = controller.createNewGame(UriComponentsBuilder.fromUriString(uriString));
			
		// Assert
		Mockito.verify(engineMock).createNewGame();
		
		HttpStatus statusCode = responseEntity.getStatusCode();
		assertEquals(expectedStatus, statusCode);
		
		HttpHeaders headers = responseEntity.getHeaders();
		assertEquals(expectedLocation, headers.getLocation().toString());
			
	}
	
	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void createNewGame_HangmanExceptionThrown_ReceiveException() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		String exceptionMessage = "this is a test exception";
		
		thrown.expect(InvalidGameInputException.class);
	    thrown.expectMessage(exceptionMessage);
		
		Mockito.when(engineMock.createNewGame()).thenThrow(new InvalidGameInputException(exceptionMessage));
		
		// Act
		controller.createNewGame(UriComponentsBuilder.fromUriString(uriString));
			
		// Assert
		Mockito.verify(engineMock).createNewGame();
					
	}
	
	@Test
	public void createNewGame_GeneralExceptionThrown_ReceiveInternalServerError() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		String exceptionMessage = "this is a test exception";
		HttpStatus expectedHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		
		thrown.expect(Exception.class);
	    thrown.expectMessage(exceptionMessage);
		
		Mockito.when(engineMock.createNewGame()).thenThrow(new Exception(exceptionMessage));
		
		// Act
		ResponseEntity<?> responseEntity = controller.createNewGame(UriComponentsBuilder.fromUriString(uriString));
			
		// Assert
		Mockito.verify(engineMock).createNewGame();
		
		assertEquals(expectedHttpStatus, responseEntity.getStatusCode());
					
	}

	@Test
	public void getAllGames_GetAllGames_Success() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		HttpStatus expectedStatus = HttpStatus.ACCEPTED;
		
		Collection<HangmanGameInstance> gamesList = new LinkedList<HangmanGameInstance>();
		HangmanGameInstance gameInstance = new HangmanGameInstance("1", "Meisje", 11);
		gamesList.add(gameInstance);
		HangmanGamesList gameInstancesToReturn = new HangmanGamesList(gamesList);
		Mockito.when(engineMock.getAllGameInstances()).thenReturn(gameInstancesToReturn);
		
		// Act
		
		ResponseEntity<?> responseEntity = controller.getAllGames(UriComponentsBuilder.fromUriString(uriString));
			
		// Assert
		Mockito.verify(engineMock).getAllGameInstances();
		
		HttpStatus statusCode = responseEntity.getStatusCode();
		assertEquals(expectedStatus, statusCode);
		
		HangmanGamesList returnedGameList = (HangmanGamesList) responseEntity.getBody();
		assertEquals(1, returnedGameList.size());
		assertEquals(returnedGameList.get(0), gameInstance);
	}
	
	@Test
	public void getAllGames_HangmanExceptionThrown_ReceiveException() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		String exceptionMessage = "this is a test exception";
		
		thrown.expect(InvalidGameInputException.class);
	    thrown.expectMessage(exceptionMessage);
		
		Mockito.when(engineMock.getAllGameInstances()).thenThrow(new InvalidGameInputException(exceptionMessage));
		
		// Act
		controller.getAllGames(UriComponentsBuilder.fromUriString(uriString));
			
		// Assert
		Mockito.verify(engineMock).createNewGame();
					
	}
	
	@Test
	public void getAllGames_GeneralExceptionThrown_ReceiveInternalServerError() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		String exceptionMessage = "this is a test exception";
		HttpStatus expectedHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		
		thrown.expect(Exception.class);
	    thrown.expectMessage(exceptionMessage);
		
		Mockito.when(engineMock.getAllGameInstances()).thenThrow(new Exception(exceptionMessage));
		
		// Act
		ResponseEntity<?> responseEntity = controller.getAllGames(UriComponentsBuilder.fromUriString(uriString));
			
		// Assert
		Mockito.verify(engineMock).createNewGame();
		
		assertEquals(expectedHttpStatus, responseEntity.getStatusCode());
					
	}
	
	@Test
	public void getGame_GetGame_SuccessReturnGameInstance() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		HttpStatus expectedStatus = HttpStatus.ACCEPTED;
		
		String gameId = "42";
		HangmanGameInstance gameInstancesToReturn = new HangmanGameInstance(gameId, "OneRingToRuleThemAll", 11);
		
		Mockito.when(engineMock.getGameInstance(gameId)).thenReturn(gameInstancesToReturn);
		
		// Act
		ResponseEntity<?> responseEntity = controller.getGame(UriComponentsBuilder.fromUriString(uriString), gameId);
			
		// Assert
		Mockito.verify(engineMock).getGameInstance(gameId);
		
		HttpStatus statusCode = responseEntity.getStatusCode();
		assertEquals(expectedStatus, statusCode);
		
		HangmanGameInstance returnedGame = (HangmanGameInstance) responseEntity.getBody();
		assertEquals(returnedGame, gameInstancesToReturn);
	}
	
	@Test
	public void getGame_HangmanExceptionThrown_ReceiveException() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		String exceptionMessage = "this is a test exception";
		String gameId = "42";
		
		thrown.expect(InvalidGameInputException.class);
	    thrown.expectMessage(exceptionMessage);
		
		Mockito.when(engineMock.getGameInstance(gameId)).thenThrow(new InvalidGameInputException(exceptionMessage));
		
		// Act
		controller.getGame(UriComponentsBuilder.fromUriString(uriString), gameId);
			
		// Assert
		Mockito.verify(engineMock).getGameInstance(gameId);
					
	}
	
	@Test
	public void getGame_GeneralExceptionThrown_ReceiveInternalServerError() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		String exceptionMessage = "this is a test exception";
		HttpStatus expectedHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		String gameId = "42";
		
		thrown.expect(Exception.class);
	    thrown.expectMessage(exceptionMessage);
		
		Mockito.when(engineMock.getGameInstance(gameId)).thenThrow(new Exception(exceptionMessage));
		
		// Act
		ResponseEntity<?> responseEntity = controller.getGame(UriComponentsBuilder.fromUriString(uriString), gameId);
			
		// Assert
		Mockito.verify(engineMock).getGameInstance(gameId);
		
		assertEquals(expectedHttpStatus, responseEntity.getStatusCode());
					
	}
	
	@Test
	public void makeGameMove_GameMoveCorrectFormat_Success() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		
		HttpStatus expectedStatus = HttpStatus.ACCEPTED;
		
		String gameId = "42";
		char charForGameMove = 'c';
		String gameMoveString = "char=" + charForGameMove;
		
		// Act
		ResponseEntity<?> responseEntity = controller.makeGameMove(UriComponentsBuilder.fromUriString(uriString), gameId, gameMoveString);
			
		// Assert
		Mockito.verify(engineMock).makeGameMove(gameId, charForGameMove);
		
		HttpStatus statusCode = responseEntity.getStatusCode();
		assertEquals(expectedStatus, statusCode);
	}
	
	@Test
	public void makeGameMove_HangmanExceptionThrown_ReceiveException() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		String exceptionMessage = "this is a test exception";
		String gameId = "42";
		
		char charForGameMove = 'c';
		String gameMoveString = "char=" + charForGameMove;
		
		thrown.expect(InvalidGameInputException.class);
	    thrown.expectMessage(exceptionMessage);
		
	    Mockito.doThrow(new InvalidGameInputException(exceptionMessage)).when(engineMock).makeGameMove(gameId, charForGameMove);
				
		// Act
	    controller.makeGameMove(UriComponentsBuilder.fromUriString(uriString), gameId, gameMoveString);
			
		// Assert
		Mockito.verify(engineMock).makeGameMove(gameId, charForGameMove);					
	}
	
	@Test
	public void makeGameMove_GeneralExceptionThrown_ReceiveInternalServerError() throws Exception
	{
		// Arrange
		String uriString = URI_STRING;
		String exceptionMessage = "this is a test exception";
		HttpStatus expectedHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		String gameId = "42";
		
		char charForGameMove = 'c';
		String gameMoveString = "char=" + charForGameMove;
		
		thrown.expect(Exception.class);
	    thrown.expectMessage(exceptionMessage);
			    
	    Mockito.doThrow(new Exception(exceptionMessage)).when(engineMock).makeGameMove(gameId, charForGameMove);
	    
		// Act
		ResponseEntity<?> responseEntity = controller.makeGameMove(UriComponentsBuilder.fromUriString(uriString), gameId, gameMoveString);
			
		// Assert
		Mockito.verify(engineMock).getGameInstance(gameId);
		
		assertEquals(expectedHttpStatus, responseEntity.getStatusCode());
					
	}
	
	
}
