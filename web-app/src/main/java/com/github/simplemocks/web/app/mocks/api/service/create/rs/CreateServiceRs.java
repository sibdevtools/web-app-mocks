package com.github.simplemocks.web.app.mocks.api.service.create.rs;

import com.github.simplemocks.common.api.rs.StandardBodyRs;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class CreateServiceRs extends StandardBodyRs<Long> {
    public CreateServiceRs(long id) {
        super(id);
    }
}
