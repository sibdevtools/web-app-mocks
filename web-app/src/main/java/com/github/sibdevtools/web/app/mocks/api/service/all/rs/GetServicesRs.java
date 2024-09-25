package com.github.sibdevtools.web.app.mocks.api.service.all.rs;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;
import com.github.sibdevtools.web.app.mocks.api.service.all.dto.HttpServiceDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Get services response
 *
 * @author sibmaks
 * @since 0.0.1
 */
public class GetServicesRs extends StandardBodyRs<ArrayList<HttpServiceDto>> {

    /**
     * Construct get services response
     *
     * @param services list of service
     */
    public GetServicesRs(List<HttpServiceDto> services) {
        super(new ArrayList<>(services));
    }
}
