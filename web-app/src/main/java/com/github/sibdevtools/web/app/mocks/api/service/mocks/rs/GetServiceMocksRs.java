package com.github.sibdevtools.web.app.mocks.api.service.mocks.rs;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;
import com.github.sibdevtools.web.app.mocks.api.service.mocks.dto.HttpServiceItemDto;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetServiceMocksRs extends StandardBodyRs<HttpServiceItemDto> {

    /**
     * Construct get service's mocks response
     *
     * @param serviceDto service dto
     */
    public GetServiceMocksRs(HttpServiceItemDto serviceDto) {
        super(serviceDto);
    }
}
