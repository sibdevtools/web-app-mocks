package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kvs;

import org.graalvm.polyglot.HostAccess;

/**
 * @author sibmaks
 * @since 0.0.23
 */
public record ValueMetaImpl(
        @HostAccess.Export long createdAt,
        @HostAccess.Export long modifiedAt,
        @HostAccess.Export Long expiredAt,
        @HostAccess.Export long version
) {
}
