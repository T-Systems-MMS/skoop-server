package io.knowledgeassets.myskills.server.exception.handler;

import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.MethodArgumentNotValidException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.domain.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

/**
 * spring MessageSource could be used here to return message if needed.
 *
 * @author hadi
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class BusinessExceptionsHandler extends ResponseEntityExceptionHandler implements IExceptionHandler {

    /**
     * when a resource(like a entity) already exists, we throw this exception.
     */
    @ExceptionHandler({DuplicateResourceException.class})
    protected ResponseEntity<Object> handleDuplicateResource(DuplicateResourceException ex, WebRequest request) {
        log.error("The resource already exists! {}", ex.getLocalizedMessage());

        ResponseError responseError = new ResponseError(CONFLICT);
        responseError.setMessage(ex.getLocalizedMessage());
        return buildResponseEntity(ex, responseError);
    }

    /**
     * Handles NoSuchResourceException.
     * If a resource (like entity) not found, we throw this exception.
     */
    @ExceptionHandler(NoSuchResourceException.class)
    protected ResponseEntity<Object> handleNoSuchResource(
            NoSuchResourceException ex) {
        log.error("The resource doesn't exist! {}", ex.getLocalizedMessage());

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
        responseError.setMessage("Validation error");
        responseError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        responseError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(ex, responseError);
    }

}
