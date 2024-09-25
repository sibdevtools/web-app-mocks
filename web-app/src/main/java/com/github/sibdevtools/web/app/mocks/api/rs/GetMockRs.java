package com.github.sibdevtools.web.app.mocks.api.rs;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;
import com.github.sibdevtools.web.app.mocks.api.dto.HttpMockDto;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetMockRs extends StandardBodyRs<HttpMockDto> {
    public GetMockRs(HttpMockDto mockDto) {
        super(mockDto);
    }
}
