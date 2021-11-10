package com.test.utils;

public class ApplicationException extends RuntimeException{
	
	/**This  is generic Exception which can be used to wrap the checked
	 * exception thrown by underlying library or java classes into
	 * unchecked  Exception with original cause  embedded in it 
	 */
	private static final long serialVersionUID = 1L;

	public ApplicationException(String msg,Throwable th){
		super(msg, th);
	}
}
