package io.knowledgeassets.myskills.server.exception;

import lombok.Builder;

/**
 * When a resource(like a entity) already exists, we throw this exception.
 *
 * @author hadi
 */
public class DuplicateResourceException extends BusinessException {

    @Builder
    public DuplicateResourceException(String message, Long code) {
        super(message, code);
    }

}
