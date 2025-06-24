package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

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
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        PublishTemplateMessageRq that = (PublishTemplateMessageRq) o;
        return Arrays.equals(key, that.key) &&
                topic.equals(that.topic) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(groupCode, that.groupCode) &&
                Objects.equals(partition, that.partition) &&
                Objects.equals(maxTimeout, that.maxTimeout) &&
                Objects.equals(templateCode, that.templateCode) &&
                Objects.equals(input, that.input) &&
                Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(groupCode);
        result = 31 * result + topic.hashCode();
        result = 31 * result + Objects.hashCode(templateCode);
        result = 31 * result + Objects.hashCode(partition);
        result = 31 * result + Objects.hashCode(timestamp);
        result = 31 * result + Arrays.hashCode(key);
        result = 31 * result + Objects.hashCode(input);
        result = 31 * result + Objects.hashCode(headers);
        result = 31 * result + Objects.hashCode(maxTimeout);
        return result;
    }
}
