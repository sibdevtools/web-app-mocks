package com.github.simplemocks.web.app.mocks.api.service.get.rs;

import com.github.simplemocks.common.api.rs.StandardBodyRs;
import com.github.simplemocks.web.app.mocks.api.service.get.dto.HttpServiceDto;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetServiceRs extends StandardBodyRs<HttpServiceDto> {
    public GetServiceRs(HttpServiceDto serviceDto) {
        super(serviceDto);
    }
}
