package io.knowledgeassets.myskills.server.exception.handler;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.domain.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This Handler is the last puzzle of all handlers(Minimum order).
 * If none of the other handlers get the error, this class will run.
 *
 * @author hadi
 */
@ControllerAdvice
@Order
@Slf4j
public class LowLevelExceptionHandler extends ResponseEntityExceptionHandler implements IExceptionHandler {

	/**
	 * We catch all business exception that we want to send HttpStatus.INTERNAL_SERVER_ERROR.
	 * <p>
	 * If you have an exception that inherits BusinessException, but wants to send a different header than
	 * the HttpStatus.INTERNAL_SERVER_ERROR, be sure to catch it in BusinessExceptionsHandler.
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler({BusinessException.class})
	protected ResponseEntity<Object> handleBusinessException(BusinessException ex) {
		log.error("Business Exception {}", ex.getLocalizedMessage());

		ResponseError responseError = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR);
		responseError.setMessage(ex.getLocalizedMessage());
		return buildResponseEntity(ex, responseError);
	}

	@ExceptionHandler({Exception.class})
	protected ResponseEntity<Object> unexpectedException(Exception ex, WebRequest request) {
		log.error("Unexpected Exception! {}", ex);

		ResponseError responseError = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR);
		responseError.setMessage(ex.getLocalizedMessage());

		return buildResponseEntity(ex, responseError);
	}
}
