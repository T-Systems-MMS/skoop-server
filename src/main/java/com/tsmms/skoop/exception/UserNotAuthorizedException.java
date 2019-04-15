package com.tsmms.skoop.exception;

/**
 * Thrown to indicate user does not have enough permissions perform an action.
 */
public class UserNotAuthorizedException extends BusinessException {

	public UserNotAuthorizedException() {
	}

	public UserNotAuthorizedException(String message) {
		super(message);
	}
}
