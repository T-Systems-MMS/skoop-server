package io.knowledgeassets.myskills.server.exception.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Whenever we face an exception, we always return an instance of this class to the client(UI).
 * So all of our ExceptionHandlers return an instance of this class.
 *
 * @author hadi
 */
@Data
public class ResponseError implements Serializable {

	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private String message;

	/**
	 * This debugMessage is used for logging purpose.
	 * It elaborates more details that are good for developers and we save it in log files.
	 */
	private String debugMessage;

	/**
	 * Details of our ResponseError
	 * If we have the reasons of why this exception has issued, we send them to UI, as a list of ResponseValidationError.
	 */
	private List<ResponseValidationError> subErrors;

	{
		timestamp = LocalDateTime.now();
	}

	private ResponseError() {
	}

	public ResponseError(HttpStatus status) {
		this.status = status;
	}

	private void addSubError(ResponseValidationError subError) {
		if (subErrors == null) {
			subErrors = new ArrayList<>();
		}
		subErrors.add(subError);
	}

	private void addValidationError(String field, String message, Object rejectedValue) {
		addSubError(new ResponseValidationError(field, message, rejectedValue));
	}

	private void addValidationError(String message) {
		addSubError(new ResponseValidationError(message));
	}

	private void addValidationError(FieldError fieldError) {
		this.addValidationError(
				fieldError.getField(),
				fieldError.getDefaultMessage(),
				fieldError.getRejectedValue()
		);
	}

	public void addValidationErrors(List<FieldError> fieldErrors) {
		fieldErrors.forEach(this::addValidationError);
	}

	public void addValidationError(List<ObjectError> globalErrors) {
		globalErrors.forEach(this::addValidationError);
	}

	private void addValidationError(ObjectError objectError) {
		this.addValidationError(objectError.getDefaultMessage());
	}

}
