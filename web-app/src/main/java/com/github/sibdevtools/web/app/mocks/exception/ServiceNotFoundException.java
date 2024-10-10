package com.github.sibdevtools.web.app.mocks.exception;

import com.github.sibdevtools.error.exception.ServiceException;
import com.github.sibdevtools.web.app.mocks.constant.Constants;

/**
 * @author sibmaks
 * @since 0.0.13
 */
public class ServiceNotFoundException extends ServiceException {

    /**
     * Construct not found error exception.
     *
     * @param serviceId service identifier
     */
    public ServiceNotFoundException(long serviceId) {
        super(404, Constants.ERROR_SOURCE, "SERVICE_NOT_FOUND", "Service %s not found".formatted(serviceId));
    }

}
