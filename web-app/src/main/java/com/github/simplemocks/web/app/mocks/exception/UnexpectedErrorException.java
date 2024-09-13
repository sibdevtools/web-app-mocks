package com.github.simplemocks.web.app.mocks.exception;

import com.github.simplemocks.error_service.exception.ServiceException;
import com.github.simplemocks.web.app.mocks.constant.Constants;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class UnexpectedErrorException extends ServiceException {

    /**
     * Construct an unexpected error exception.
     *
     * @param systemMessage system message
     */
    public UnexpectedErrorException(String systemMessage) {
        super(Constants.ERROR_SOURCE, "UNEXPECTED_ERROR", systemMessage);
    }

    /**
     * Construct an unexpected error exception with cause.
     *
     * @param systemMessage system message
     * @param cause         cause
     */
    public UnexpectedErrorException(String systemMessage, Throwable cause) {
        super(Constants.ERROR_SOURCE, "UNEXPECTED_ERROR", systemMessage, cause);
    }
}
