package com.github.simplemocks.web.app.mocks.api.rs;

import com.github.simplemocks.common.api.rs.StandardBodyRs;
import com.github.simplemocks.web.app.mocks.api.dto.HttpMockDto;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public class GetMockUrlRs extends StandardBodyRs<String> {
    public GetMockUrlRs(String url) {
        super(url);
    }
}
