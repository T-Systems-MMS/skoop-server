package com.tsmms.skoop.exception;

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

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) { super(message, cause); }

}
