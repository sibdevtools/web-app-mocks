package com.github.sibdevtools.web.app.mocks.exception;

import com.github.sibdevtools.error.exception.ServiceException;
import com.github.sibdevtools.web.app.mocks.constant.Constants;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class NotFoundException extends ServiceException {

    /**
     * Construct not found error exception.
     *
     * @param systemMessage system message
     */
    public NotFoundException(String systemMessage) {
        super(404, Constants.ERROR_SOURCE, "NOT_FOUND", systemMessage);
    }

    /**
     * Construct not found exception with cause.
     *
     * @param systemMessage system message
     * @param cause         cause
     */
    public NotFoundException(String systemMessage, Throwable cause) {
        super(Constants.ERROR_SOURCE, "404, NOT_FOUND", systemMessage, cause);
    }
}
