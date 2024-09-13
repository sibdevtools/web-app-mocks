package com.github.simplemocks.web.app.mocks.api.rs;

import com.github.simplemocks.common.api.rs.StandardBodyRs;
import com.github.simplemocks.web.app.mocks.api.dto.HttpMockDto;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetMockRs extends StandardBodyRs<HttpMockDto> {
    public GetMockRs(HttpMockDto mockDto) {
        super(mockDto);
    }
}
