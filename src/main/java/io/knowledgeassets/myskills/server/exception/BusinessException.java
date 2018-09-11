package io.knowledgeassets.myskills.server.exception;

/**
 * All business exceptions should inherits this class.
 *
 * @author hadi
 */
public abstract class BusinessException
        extends GeneralException {

    public BusinessException() {
        super();
    }

    public BusinessException(String message, Long code) {
        super(message, code);
    }
}
