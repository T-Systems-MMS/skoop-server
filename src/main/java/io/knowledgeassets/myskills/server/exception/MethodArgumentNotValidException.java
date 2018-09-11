package io.knowledgeassets.myskills.server.exception;

import lombok.Builder;
import org.springframework.validation.BindingResult;

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
