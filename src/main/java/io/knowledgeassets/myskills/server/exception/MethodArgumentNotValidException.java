package io.knowledgeassets.myskills.server.exception;

import lombok.Builder;
import org.springframework.validation.BindingResult;

/**
 * When an object fails @Valid validation, we throw this exception.
 * This exception only is used in {@link io.knowledgeassets.myskills.server.aspect.BindingResultAspect}
 */
public class MethodArgumentNotValidException extends BusinessException {

	private BindingResult bindingResult;

	@Builder
	private MethodArgumentNotValidException(Long code, BindingResult bindingResult) {
		super();
		this.setCode(code);
		this.bindingResult = bindingResult;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}
}
