package com.github.simplemocks.web.app.mocks.api.rs;

import com.github.simplemocks.common.api.rs.StandardBodyRs;
import com.github.simplemocks.web.app.mocks.api.dto.HttpServiceDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetServicesRs extends StandardBodyRs<ArrayList<HttpServiceDto>> {
    public GetServicesRs(List<HttpServiceDto> serviceDto) {
        super(new ArrayList<>(serviceDto));
    }
}
