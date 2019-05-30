package com.tsmms.skoop.exception;

/**
 * Thrown to indicate there is an issue when sending an e-mail.
 */
public class EmailSendingFailedException extends BusinessException {

	public EmailSendingFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
