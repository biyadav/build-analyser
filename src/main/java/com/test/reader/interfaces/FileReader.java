package com.test.reader.interfaces;

import java.io.IOException;
import com.test.EventQueue;

public interface FileReader {

	public void readFile(String fileLocation,EventQueue eventQueue) throws IOException;
}
