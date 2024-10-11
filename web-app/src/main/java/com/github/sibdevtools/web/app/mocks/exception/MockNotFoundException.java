package com.github.sibdevtools.web.app.mocks.exception;

import com.github.sibdevtools.error.exception.ServiceException;
import com.github.sibdevtools.web.app.mocks.constant.Constants;

/**
 * @author sibmaks
 * @since 0.0.13
 */
public class MockNotFoundException extends ServiceException {

    /**
     * Construct not found error exception.
     *
     * @param mockId mock identifier
     */
    public MockNotFoundException(long mockId) {
        super(404, Constants.ERROR_SOURCE, "MOCK_NOT_FOUND", "Mock %s not found".formatted(mockId));
    }

}
