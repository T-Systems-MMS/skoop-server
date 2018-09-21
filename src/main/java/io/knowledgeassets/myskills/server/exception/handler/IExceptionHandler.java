package io.knowledgeassets.myskills.server.exception.handler;

import io.knowledgeassets.myskills.server.exception.GeneralException;
import io.knowledgeassets.myskills.server.exception.domain.ResponseError;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

interface IExceptionHandler {

	Logger getLogger();

	default ResponseEntity<Object> buildResponseEntity(Exception ex, ResponseError responseError) {
		if (ex.getCause() != null) {
			responseError.setDebugMessage(ex.getCause().getLocalizedMessage());
		}
		return new ResponseEntity<>(responseError, responseError.getStatus());
	}

	default void doLog(GeneralException ex, String message) {
		if (ex.getDebugMessage() != null) {
			getLogger().error(ex.getDebugMessage());
		}
		if (message != null) {
			getLogger().error(message, ex);
		}
		if (ex.getSuggestion() != null) {
			getLogger().error(ex.getSuggestion());
		}
	}

	default void doLog(Exception ex, String message) {
		if (message != null) {
			getLogger().error(message, ex);
		}
	}
} 
