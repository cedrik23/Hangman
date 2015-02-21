package com.quandidate.hangman.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Amir
 * indicated there no more game move to be done on requested game
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoMoreGameMovesException extends HangmanBaseException {
	private static final long serialVersionUID = -2674842043002611537L;
	
	public NoMoreGameMovesException(String gameId)
	{
		super("No more game moves on game: " + gameId);
	}
}
