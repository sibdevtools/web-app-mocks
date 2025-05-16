package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka;

import org.graalvm.polyglot.HostAccess;

/**
 * @author sibmaks
 * @since 0.0.22
 */
public record PublishMessageRs(
        @HostAccess.Export long offset,
        @HostAccess.Export long timestamp,
        @HostAccess.Export int serializedKeySize,
        @HostAccess.Export int serializedValueSize,
        @HostAccess.Export int partition
) {
}
