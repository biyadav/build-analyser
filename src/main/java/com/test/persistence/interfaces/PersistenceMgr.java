package com.test.persistence.interfaces;

import java.util.List;

import com.test.persistence.dto.EventDto;

public interface PersistenceMgr {
	
	void writeRecords(List<EventDto> events);
	void createTable();

}
