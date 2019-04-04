package com.tsmms.skoop.exception;

import lombok.Builder;

/**
 * When a resource(like a entity) already exists, we throw this exception.
 *
 * @author hadi
 */
public class DuplicateResourceException extends BusinessException {

    @Builder
    public DuplicateResourceException(String message) {
        super(message);
    }

}
