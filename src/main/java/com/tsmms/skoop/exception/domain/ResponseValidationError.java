package com.tsmms.skoop.exception.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Details of our ResponseError
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ResponseValidationError {
    private String field;
	private String message;
	private Object rejectedValue;

    ResponseValidationError(String message) {
        this.message = message;
    }
} 
