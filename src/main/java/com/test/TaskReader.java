package com.test;

import java.io.IOException;
import org.apache.log4j.Logger;
import com.test.reader.implementations.FileReaderImpl;
import com.test.utils.ApplicationException;

public class TaskReader implements Runnable {
	
	private String filePath;
	private EventQueue eventQueue;
	private FileReaderImpl reader ;
	
	public static final Logger LOGGER  = Logger.getLogger(TaskReader.class);
	
	public TaskReader( String filePath, EventQueue eventQueue,FileReaderImpl reader){
		 this.filePath = filePath;
		 this.eventQueue =  eventQueue;
		 this.reader = reader;
	}

	@Override
	public void run() {
		try {
			reader.readFile(filePath, eventQueue);
		} catch (IOException e) {
			LOGGER.error("Error in reading file  with absolute path "+filePath , e);
		    throw new ApplicationException("FileReading Failed", e);
		}

	}

}
