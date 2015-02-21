package com.quandidate.hangman.utilities;

public interface IConfigurationContainer {

	/**
	 * get the value of the configuration based on it's key
	 * @param key
	 * @return the configuration value
	 */
	public abstract String getValue(String key);
}
