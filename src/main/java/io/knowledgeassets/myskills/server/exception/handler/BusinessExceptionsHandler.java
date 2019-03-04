package io.knowledgeassets.myskills.server.exception.handler;

import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.exception.MethodArgumentNotValidException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.UserCommunityAccessDeniedException;
import io.knowledgeassets.myskills.server.exception.domain.ResponseError;
import io.knowledgeassets.myskills.server.exception.domain.ResponseValidationError;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;

/**
 * spring MessageSource could be used here to return message if needed.
 *
 * @author hadi
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class BusinessExceptionsHandler implements IExceptionHandler {

	/**
	 * when a resource(like a entity) already exists, we throw this exception.
	 */
	@ExceptionHandler({DuplicateResourceException.class})
	protected ResponseEntity<Object> handleDuplicateResource(DuplicateResourceException ex, WebRequest request) {
		String logMessage = String.format("{%s} The resource already exists! %s", CONFLICT.value() + " " + CONFLICT.getReasonPhrase(), ex.getLocalizedMessage());
		doLog(ex, logMessage);

		ResponseError responseError = new ResponseError(CONFLICT);
		responseError.setMessage(ex.getLocalizedMessage());
		return buildResponseEntity(ex, responseError);
	}

	/**
	 * Handles NoSuchResourceException. If a resource (like entity) not found, we throw this exception.
	 */
	@ExceptionHandler(NoSuchResourceException.class)
	protected ResponseEntity<Object> handleNoSuchResource(
			NoSuchResourceException ex) {
		String logMessage = String.format("{%s} The resource doesn't exist! %s", NOT_FOUND.value() + " " + NOT_FOUND.getReasonPhrase(), ex.getLocalizedMessage());
		doLog(ex, logMessage);

		ResponseError responseError = new ResponseError(NOT_FOUND);
		responseError.setMessage(ex.getLocalizedMessage());
		return buildResponseEntity(ex, responseError);
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		ResponseError responseError = new ResponseError(BAD_REQUEST);
		responseError.setMessage("Validation error!");
		responseError.addValidationErrors(ex.getBindingResult().getFieldErrors());
		responseError.addValidationError(ex.getBindingResult().getGlobalErrors());

		String logMessage = null;
		if (!CollectionUtils.isEmpty(responseError.getSubErrors())) {
			StringBuilder errorDetails = new StringBuilder("Error Details: ");
			for (ResponseValidationError subError : responseError.getSubErrors()) {
				errorDetails.append(subError.getField()).append(" ").append(subError.getMessage());
			}
			logMessage = String.format("{%s} Validation error - %s", BAD_REQUEST.value() + " " + BAD_REQUEST.getReasonPhrase(), errorDetails.toString());
		} else {
			logMessage = "Validation error";
		}
		doLog(ex, logMessage);

		return buildResponseEntity(ex, responseError);
	}

	/**
	 * Handles EmptyInputException. If some mandatory input parameter is missing or empty we throw this exception.
	 */
	@ExceptionHandler(EmptyInputException.class)
	protected ResponseEntity<Object> handleEmptyInput(EmptyInputException ex) {
		String logMessage = String.format("{%s} Some mandatory input was missing! %s",
				BAD_REQUEST.value() + " " + BAD_REQUEST.getReasonPhrase(), ex.getLocalizedMessage());
		doLog(ex, logMessage);

		ResponseError responseError = new ResponseError(BAD_REQUEST);
		responseError.setMessage(ex.getLocalizedMessage());
		return buildResponseEntity(ex, responseError);
	}

	/**
	 * Handles UserIsNotCommunityManagerException. If user has some issues with an access to the community we throw this exception.
	 */
	@ExceptionHandler(UserCommunityAccessDeniedException.class)
	protected ResponseEntity<Object> handleUserIsNotCommunityManager(UserCommunityAccessDeniedException ex) {
		String logMessage = String.format("{%s} User has issues with an access to the community! %s",
				FORBIDDEN.value() + " " + FORBIDDEN.getReasonPhrase(), ex.getLocalizedMessage());
		doLog(ex, logMessage);

		ResponseError responseError = new ResponseError(FORBIDDEN);
		responseError.setMessage(ex.getLocalizedMessage());
		return buildResponseEntity(ex, responseError);
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
