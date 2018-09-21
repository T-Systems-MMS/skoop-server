package io.knowledgeassets.myskills.server.exception.handler;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.domain.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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
		String logMessage = String.format("{%s} Business Exception! %s", INTERNAL_SERVER_ERROR.value() + " " + INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getLocalizedMessage());
		doLog(ex, logMessage);

		ResponseError responseError = new ResponseError(INTERNAL_SERVER_ERROR);
		responseError.setMessage(ex.getLocalizedMessage());
		return buildResponseEntity(ex, responseError);
	}

	@ExceptionHandler({Exception.class})
	protected ResponseEntity<Object> unexpectedException(Exception ex, WebRequest request) {
		String logMessage = String.format("{%s} Unexpected Exception! %s", INTERNAL_SERVER_ERROR.value() + " " + INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getLocalizedMessage());
		doLog(ex, logMessage);

		ResponseError responseError = new ResponseError(INTERNAL_SERVER_ERROR);
		responseError.setMessage(ex.getLocalizedMessage());

		return buildResponseEntity(ex, responseError);
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
