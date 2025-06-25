package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kvs;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.23
 */
public record SetValueRq(
        String space,
        String key,
        Serializable value,
        Long expiredAt
) {
}
