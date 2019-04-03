package com.tsmms.skoop.exception;

/**
 * This is the father of all our exceptions.
 *
 * @author hadi
 */
public abstract class GeneralException
        extends RuntimeException {

	/**
	 * This field is used only for logging purpose.
	 * It elaborates more details that are good for developers when an exception occurs. We save it in log system.
	 */
	private String debugMessage;

	/**
	 * This field is used only for logging purpose.
	 * If you have any specific suggestion, you can put it in this field.
	 */
	private String suggestion;

    public GeneralException() {
        super();
    }

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable cause) {super(message, cause); }

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
}
