package com.quandidate.hangman.entities;

import java.util.ArrayList;
import java.util.Collection;

/**
 * represents a list of hangman games
 * @author Amir
 *
 */
public class HangmanGamesList extends ArrayList<HangmanGameInstance> {

	private static final long serialVersionUID = 5703259979938814859L;
	
	public HangmanGamesList(Collection<HangmanGameInstance> gamesList)
	{
		super(gamesList);
	}

}
