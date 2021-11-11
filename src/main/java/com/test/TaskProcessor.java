package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.test.models.FileLine;
import com.test.persistence.dto.EventDto;
import com.test.persistence.interfaces.PersistenceMgr;
import com.test.reader.interfaces.FileReader;
import com.test.utils.ApplicationException;
import com.test.utils.Constants;

public class TaskProcessor implements Runnable {

	
	public TaskProcessor(int batchCommitSize, EventQueue eventQueue, PersistenceMgr pm) {
		super();
		this.batchCommitSize = batchCommitSize;
		this.eventQueue = eventQueue;
		this.pm = pm;
	}

	private int batchCommitSize ;
	private EventQueue eventQueue ;
	private PersistenceMgr pm ;
	
	public static final Logger LOGGER  = Logger.getLogger(TaskProcessor.class);
			
	@Override
	public void run() {
		List<EventDto> eventsToInsert = new ArrayList<EventDto>(batchCommitSize);	
		Map<String,FileLine> eventInProcessing = new HashMap<String,FileLine>();
			// if file reading completed and the queue has no 
			// elements then both reader and processor have done their task 
			// otherwise  there will not be element and processor will stuck 
			// after calling take ()
			for (; !eventQueue.isAllRead() ;){
				
				try {
					
				   if(eventsToInsert.size() == batchCommitSize){
					   persistChunk(eventsToInsert);
					   
				   }
				   
				   LOGGER.info("Picking Next Line from Queue  ");
				   FileLine line = eventQueue.takeFirst();
				   String key = line.getId()+line.getHost();
				   
				   LOGGER.debug("read line is  "+line);
				   FileLine existingRecord = eventInProcessing.remove(key);
				   // we did not previously read the event with same id and host
				   // hence store temporarily for a given id and host as we need
				   // both STARTED and COMPLETED events for calculating duration
				   if(existingRecord == null){
					   eventInProcessing.put(key, line);
				   }
				   else{
					   // already read  with same id and host  so we can calculate the duration
					   EventDto eventDto = new EventDto();
					   eventDto.setId(existingRecord.getId());
					   eventDto.setHost(existingRecord.getHost());
					   eventDto.setType(existingRecord.getType() == null?line.getType():existingRecord.getType());
					   // set the time stamp based on state  in dto as start or end of the event
					   if(Constants.STATE_STARTED.equalsIgnoreCase(existingRecord.getState())){
						   eventDto.setStartTimeStamp(existingRecord.getTimestamp());
						   eventDto.setEndTimeStamp(line.getTimestamp());
					   }
					   else{
						   eventDto.setStartTimeStamp(line.getTimestamp());
						   eventDto.setEndTimeStamp(existingRecord.getTimestamp());
	
					   }
					   eventDto.setDuration((int)(eventDto.getEndTimeStamp()-eventDto.getStartTimeStamp()));
					   LOGGER.debug("Adding EventDto for insert "+eventDto);
					   eventsToInsert.add(eventDto); 
				   }
				} catch (InterruptedException e) {
					LOGGER.error(" Processing Failed ", e);
					throw new ApplicationException("Issue in processing the read record", e);
				}
			}
			
			if(eventsToInsert.size() > 0){
			 persistChunk(eventsToInsert);
			}
			
			LOGGER.info("UPDATING BUILD ANALYSIS TO DATABASE COMPLETED ");
		}

	

	private void persistChunk(List<EventDto> eventsToInsert) {
		LOGGER.info("Calling Persistence to insert event details ");
		pm.writeRecords(eventsToInsert);
		eventsToInsert.clear();
	}

}
