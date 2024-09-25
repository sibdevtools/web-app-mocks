package com.github.sibdevtools.web.app.mocks.api.rs;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class CreateMockRs extends StandardBodyRs<Long> {
    public CreateMockRs(long id) {
        super(id);
    }
}
