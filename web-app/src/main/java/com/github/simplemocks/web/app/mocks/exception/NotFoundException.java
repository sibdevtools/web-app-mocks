package com.github.simplemocks.web.app.mocks.exception;

import com.github.simplemocks.error_service.exception.ServiceException;
import com.github.simplemocks.web.app.mocks.constant.Constants;

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
