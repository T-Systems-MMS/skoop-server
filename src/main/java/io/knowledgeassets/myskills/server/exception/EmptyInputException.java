package io.knowledgeassets.myskills.server.exception;

import lombok.Builder;

/**
 * When a <b>field data</b> is empty, but we expect to be full, we throw this exception.
 *
 * @author hadi
 */
public class EmptyInputException extends BusinessException {

	@Builder
	public EmptyInputException(String message, Long code) {
		super(message, code);
	}
}
