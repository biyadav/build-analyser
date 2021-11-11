package com.test;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import com.test.persistence.implementations.PersistenceMgrImpl;
import com.test.persistence.interfaces.PersistenceMgr;
import com.test.reader.implementations.FileReaderImpl;
import com.test.utils.ApplicationException;


public class AppLauncher {
	
	public static final Logger LOGGER  = Logger.getLogger(AppLauncher.class);
	
	public static void main(String [] args){
		  
		validateFilePath(args);
		
		String filePath = args[0];
		int batchCommitSize = 50;
		
		EventQueue eventQueue = new EventQueue();
		FileReaderImpl reader = new FileReaderImpl();
		PersistenceMgr pm = new PersistenceMgrImpl();
		
		initLogger();
		createTable(pm);
		
		
		Thread readerTask = new Thread(new TaskReader(filePath, eventQueue, reader));
		readerTask.start();	
			
		Thread processorTask = new Thread(new TaskProcessor(batchCommitSize, eventQueue, pm));
	    processorTask.start();	
    
	}

	private static void createTable(PersistenceMgr pm) {
		pm.createTable();	
	}

	private static void validateFilePath(String[] args) {
		
		if(args.length == 0){
			LOGGER.error("FILE ABSOLUTE PATH MISSING , Should be Provided as command line arg");
			throw new ApplicationException("File Path missing",new IllegalStateException());
		}
		if(args.length > 1){
			LOGGER.warn("More than one command line  passed  considering first as FilePath ignoring rest ");
		}
	}

	private static void initLogger() {
		
		  FileAppender fa = new FileAppender();
		  fa.setName("FileLogger");
		  fa.setFile("BuildAnalyzer.log");
		  fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		  fa.setAppend(true);
		  fa.activateOptions();
		  Logger.getRootLogger().addAppender(fa);
	}

}
