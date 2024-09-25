package com.github.sibdevtools.web.app.mocks.api.rs;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public class GetMockUrlRs extends StandardBodyRs<String> {
    public GetMockUrlRs(String url) {
        super(url);
    }
}
