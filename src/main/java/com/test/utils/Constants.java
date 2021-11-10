package com.test.utils;

public interface Constants {
	
	String STATE_STARTED ="STARTED";
	String STATE_COMPLETED ="FINISHED";
	
    String CREATE_TABLE_STMT  = "CREATE TABLE BUILD_REPORT  "
            + "  (EVENT_ID        VARCHAR(40),"
            + "   EVENT_DURATION  INTEGER,"
            + "   HOST          VARCHAR(40),"
            + "   TYPE          VARCHAR(35),"
            + "   ALERT         VARCHAR(10))";
    
    String BATCH_INSERT_STMT = "INSERT INTO BUILD_REPORT(EVENT_ID, EVENT_DURATION,HOST,TYPE,ALERT) VALUES(";
	
    String  ALERT_TRUE = "TRUE";
    String  ALERT_FALSE = "FALSE";
}
