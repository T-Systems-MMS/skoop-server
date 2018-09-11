package io.knowledgeassets.myskills.server.exception;

public class InvalidInputException extends BusinessException {

    public InvalidInputException(String message, Long code) {
        super(message, code);
    }
}
