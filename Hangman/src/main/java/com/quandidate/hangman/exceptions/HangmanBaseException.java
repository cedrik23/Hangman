package com.quandidate.hangman.exceptions;

/**
 * @author Amir
 * base class for hangman game exceptions
 */
public abstract class HangmanBaseException extends RuntimeException {
	
	private static final long serialVersionUID = -2674842043002691532L;

	public HangmanBaseException(String message)
	{
		super(message);
	}
}
