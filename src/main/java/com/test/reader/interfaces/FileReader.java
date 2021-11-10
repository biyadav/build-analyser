package com.test.reader.interfaces;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.test.models.FileLine;

public interface FileReader {

	public void readFile(String fileLocation,BlockingQueue<FileLine> queue) throws IOException;
}
