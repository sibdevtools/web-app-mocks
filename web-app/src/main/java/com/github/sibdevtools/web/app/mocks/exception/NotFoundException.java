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
}
