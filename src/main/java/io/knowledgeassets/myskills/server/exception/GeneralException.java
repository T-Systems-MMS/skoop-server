package io.knowledgeassets.myskills.server.exception;

/**
 * This is the father of all our exceptions.
 *
 * @author hadi
 */
public abstract class GeneralException
        extends RuntimeException {

    public GeneralException() {
        super();
    }

    public GeneralException(String message) {
        super(message);
    }
}
