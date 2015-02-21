package com.quandidate.hangman.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationContainer implements IConfigurationContainer{	

	private Properties m_configFile = new Properties();
	
	public ConfigurationContainer(String configPath) throws IOException	{
		
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream is = classLoader.getResourceAsStream(configPath);
		m_configFile.load(is);		
	}
	
	public String getValue(String key)
	{
		return m_configFile.getProperty(key);
	}
	
}
