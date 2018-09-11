package io.knowledgeassets.myskills.server.exception;

/**
 * This is the father of all our exceptions.
 *
 * @author hadi
 */
public abstract class GeneralException
        extends Exception {

    /**
     * Error Code
     * <p>
     * each time that in a useCase we throw an exception, it should have a unique code.
     * so we send it to customer, and if customer faces an exception, they say the error code and after that
     * we can find it easily in our code.
     */
    private Long code;

    public GeneralException() {
        super();
    }

    public GeneralException(Long code) {
        super();
        this.code = code;
    }

    public GeneralException(String message, Long code) {
        super(message);
        this.code = code;
    }


    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }
}
