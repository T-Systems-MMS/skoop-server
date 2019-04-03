package com.tsmms.skoop.exception;

/**
 * Thrown to indicate there was some issue with a user profile document.
 */
public class UserProfileDocumentException extends BusinessException {

	public UserProfileDocumentException(String message) {
		super(message);
	}

	public UserProfileDocumentException(String message, Throwable cause) {
		super(message, cause);
	}

}
