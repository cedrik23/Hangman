package com.quandidate.hangman.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Amir
 * exception to indicate an invalid game input
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidGameInputException extends HangmanBaseException {
	
	private static final long serialVersionUID = -2674842043002691537L;
	
	public InvalidGameInputException(String input)
	{
		super("Invalid game input: " + input);
	}

}
