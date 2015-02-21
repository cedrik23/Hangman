package com.quandidate.hangman.wordrepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.quandidate.hangman.utilities.ConfigurationContainer;

public class FileWordRepository implements IWordRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(FileWordRepository.class);
	
	private final List<String> wordList = new ArrayList<String>();
	
	@Autowired
	private ConfigurationContainer configContainer;
	
	public FileWordRepository(String filePath) throws Exception
	{
		logger.info("Starting word repository, getting all words from file: " + filePath);
		
		if(filePath == null || filePath.isEmpty())
		{
			String message = "No configuration found for FileWordRepository file path";
			logger.error(message);
			throw new Exception(message);
		}
		
		try {
			
			ClassLoader classLoader = this.getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(filePath);
			
			String line;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			while ((line = bufferedReader.readLine()) != null) {
				wordList.add(line.trim());
			}
			
		} catch (Exception e) {
			logger.error("Error while trying to read word list file: " + filePath, e);
			throw e;
		}
	}
	 
	@Override
	public String getRandomWord() {
		Random r = new Random();
		int randomWordIndex = r.nextInt(wordList.size());
		
		return wordList.get(randomWordIndex);
	}

}
