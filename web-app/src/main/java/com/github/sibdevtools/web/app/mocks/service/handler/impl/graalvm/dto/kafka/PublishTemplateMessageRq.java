package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka;

import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.22
 */
public record PublishTemplateMessageRq(
        String groupCode,
        String topic,
        String templateCode,
        Integer partition,
        Long timestamp,
        byte[] key,
        Map<String, Object> input,
        Map<String, byte[]> headers,
        Integer maxTimeout
) {
}
