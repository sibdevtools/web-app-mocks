package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kvs;

import org.graalvm.polyglot.HostAccess;

/**
 * @author sibmaks
 * @since 0.0.23
 */
public record ValueHolderImpl(
        @HostAccess.Export byte[] value,
        @HostAccess.Export ValueMetaImpl meta
) {
}
