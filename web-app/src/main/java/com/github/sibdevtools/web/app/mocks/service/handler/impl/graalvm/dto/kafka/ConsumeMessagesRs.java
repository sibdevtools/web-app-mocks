package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka;

import lombok.Builder;
import org.graalvm.polyglot.HostAccess;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.22
 */
@Builder
public record ConsumeMessagesRs(
        @HostAccess.Export String topic,
        @HostAccess.Export List<ConsumedMessage> messages
) {
}
