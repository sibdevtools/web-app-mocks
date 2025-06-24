package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.22
 */
public record ConsumeMessagesRq(
        String groupCode,
        List<String> bootstrapServers,
        String topic,
        int maxMessages,
        Integer maxTimeout,
        Direction direction
) {
    enum Direction {
        EARLIEST,
        LATEST
    }
}
