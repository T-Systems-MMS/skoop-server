package io.knowledgeassets.myskills.server.exception;

import lombok.Builder;

/**
 * in exception zamanhaee estefade mishavad ke vorudi empty ya null bashad.
 *
 * @author hadi
 */
public class EmptyInputException
        extends InvalidInputException {

    @Builder
    public EmptyInputException(String message, Long code) {
        super(message, code);
    }

}
