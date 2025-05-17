package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka;

import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.22
 */
public record PublishMessageRq(
        String groupCode,
        List<String> bootstrapServers,
        String topic,
        //asd
        Integer partition,
        Long timestamp,
        byte[] key,
        byte[] value,
        Map<String, byte[]> headers,
        Integer maxTimeout
) {
}
