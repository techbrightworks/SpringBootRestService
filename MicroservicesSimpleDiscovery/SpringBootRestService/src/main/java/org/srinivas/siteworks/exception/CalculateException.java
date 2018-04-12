package org.srinivas.siteworks.exception;


public class CalculateException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new calculate exception.
	 *
	 * @param message the message
	 * @param t the t
	 */
	public CalculateException(String message,Throwable t){
		super(message,t);
	}
	
	/**
	 * Instantiates a new calculate exception.
	 *
	 * @param message the message
	 */
	public CalculateException(String message){
		super(message);
	}

}
