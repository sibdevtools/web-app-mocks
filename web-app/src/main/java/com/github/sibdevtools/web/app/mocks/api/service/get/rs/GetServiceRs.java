package com.github.sibdevtools.web.app.mocks.api.service.get.rs;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;
import com.github.sibdevtools.web.app.mocks.api.service.get.dto.HttpServiceDto;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetServiceRs extends StandardBodyRs<HttpServiceDto> {
    public GetServiceRs(HttpServiceDto serviceDto) {
        super(serviceDto);
    }
}
