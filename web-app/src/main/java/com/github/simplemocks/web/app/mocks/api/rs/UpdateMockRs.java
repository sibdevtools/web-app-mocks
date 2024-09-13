package com.github.simplemocks.web.app.mocks.api.rs;

import com.github.simplemocks.common.api.rs.StandardBodyRs;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class UpdateMockRs extends StandardBodyRs<Long> {
    public UpdateMockRs(long id) {
        super(id);
    }
}
