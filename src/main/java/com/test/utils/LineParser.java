package com.test.utils;

import com.test.models.FileLine;

public class LineParser {
	
	private static  String FIELD_SEPARATOR ="," ;
	private static  String KEY_VALUE_SEPARATOR =":";
	private static  String KEY_ID = "id";
	private static  String KEY_STATE = "state";
	private static  String KEY_TYPE = "type";
	private static  String KEY_HOST = "host";
	private static  String KEY_TIMESTAMP ="timestamp";
	
	static {
		String fieldSep = System.getProperty("field.separator");
		String keyValueSep = System.getProperty("keyValue.separator");
		
		if(fieldSep != null){
			FIELD_SEPARATOR = fieldSep;
		}
		
		if(keyValueSep != null){
			KEY_VALUE_SEPARATOR = keyValueSep;
		}
	}
	
	
	
	private LineParser(){}
	
	public static FileLine parse(String record){
		
		FileLine fileLine = new FileLine();
		 System.out.println("read record is "+ record);
		//replace  the staring and closing curly braces
		 record = record.replace("{", "");
		 record = record.replace("}", "");
		
		
		String [] KVs = record.split(FIELD_SEPARATOR);
		
		for(String kv :KVs){
			String [] nameAndValue = kv.split(KEY_VALUE_SEPARATOR);
			setField(nameAndValue[0],nameAndValue[1],fileLine);
		}
		
		return fileLine;
	}

	private static void setField(String fieldName, String fieldValue,FileLine fileLine) {
		
		// remove leading and trailing " 
		fieldName = fieldName.substring(1, fieldName.length()-1);
		
		System.out.println("fieldName  "+fieldName+"  fieldValue "+fieldValue);
		if(KEY_ID.equalsIgnoreCase(fieldName)){
			fileLine.setId(fieldValue);
		}
		if(KEY_STATE.equalsIgnoreCase(fieldName)){
			fileLine.setState(fieldValue);	
		}
		if(KEY_TYPE.equalsIgnoreCase(fieldName)){
			fileLine.setType(fieldValue);
		}
		if(KEY_HOST.equalsIgnoreCase(fieldName)){
			fileLine.setHost(fieldValue);
		}
        if(KEY_TIMESTAMP.equalsIgnoreCase(fieldName)){
			fileLine.setTimestamp(Long.valueOf(fieldValue.trim()));
		}
		
	}

}
