package com.tsmms.skoop.exception;

import com.tsmms.skoop.aspect.BindingResultAspect;
import lombok.Builder;
import org.springframework.validation.BindingResult;

/**
 * When an object fails @Valid validation, we throw this exception.
 * This exception only is used in {@link BindingResultAspect}
 */
public class MethodArgumentNotValidException extends BusinessException {

	private BindingResult bindingResult;

	@Builder
	private MethodArgumentNotValidException( BindingResult bindingResult) {
		super();
		this.bindingResult = bindingResult;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}
}
