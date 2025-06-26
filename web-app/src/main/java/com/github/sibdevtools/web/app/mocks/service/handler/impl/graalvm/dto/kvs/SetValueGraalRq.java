package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kvs;

/**
 * @author sibmaks
 * @since 0.0.23
 */
public record SetValueGraalRq(
        String space,
        String key,
        byte[] value,
        Long expiredAt
) {
}
