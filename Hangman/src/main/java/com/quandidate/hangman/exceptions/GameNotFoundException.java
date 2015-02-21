package com.quandidate.hangman.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * @author Amir
 * exception indicating that the requested game was not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends HangmanBaseException {

	private static final long serialVersionUID = -2674842043002691536L;

	public GameNotFoundException(String gameId)
	{
		super("Could not find game with id: " + gameId);
	}
}
