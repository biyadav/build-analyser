package com.test;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.test.models.FileLine;

public class EventQueue {

	private BlockingQueue<FileLine> queue = new ArrayBlockingQueue<FileLine>(1000);;
	 
	private volatile boolean isCompleted = false;

	public void addToLast(FileLine record) throws InterruptedException {
		 queue.put(record);;
	}
	
	public FileLine takeFirst() throws InterruptedException  {
		 return queue.take();
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	
	public boolean isAllRead(){
		return queue.isEmpty() && isCompleted ;
	}
	 
	 
}
