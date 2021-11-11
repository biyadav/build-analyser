package com.test.reader.implementations;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.test.EventQueue;
import com.test.models.FileLine;
import com.test.reader.interfaces.FileReader;
import com.test.utils.ApplicationException;
import com.test.utils.LineParser;

public class FileReaderImpl implements FileReader{
	
	public static final Logger LOGGER = Logger.getLogger(FileReaderImpl.class);
	
	private  volatile boolean isCompleted = false;
	
	public boolean isCompleted(){
		return isCompleted;
	}
	
	public void readFile(String fileLocation,EventQueue eventQueue ) throws IOException{
		
		LOGGER.debug("About to read the file  : "+ fileLocation);
		
		BufferedReader bufferedReader = null;
		try(InputStream inputStream = new FileInputStream(fileLocation);) {
		    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		    bufferedReader
    		.lines()
    		.parallel()
            .map(line -> LineParser.parse(line))
            .forEach(fileLine -> addToQueue(fileLine,eventQueue));
		          
		     eventQueue.setCompleted(true);
		     LOGGER.info("File reading completed ");
		     bufferedReader.close();
		} catch (FileNotFoundException e) {
			if(bufferedReader != null){
				bufferedReader.close();
			}
			eventQueue.setCompleted(true);
			LOGGER.error("File reading failed ", e);
			throw new ApplicationException("File reading Failed", e);
		}
		
	}

	private void addToQueue(FileLine fileLine, EventQueue eventQueue) {
		
		try {
			eventQueue.addToLast(fileLine);
		} catch (InterruptedException e) {
			LOGGER.error("Adding current Line to queue Failed ", e);
			throw new ApplicationException("Enqueue operation failed ", e);
		}
	}

}
