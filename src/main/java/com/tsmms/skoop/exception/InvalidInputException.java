package com.tsmms.skoop.exception;

import lombok.Builder;

/**
 * Thrown to indicate that a method has been passed an illegal or inappropriate argument.
 *
 * @author hadi on 9/21/2018.
 */
public class InvalidInputException extends BusinessException {

	@Builder
	public InvalidInputException(String message) {
		super(message);
	}
}
