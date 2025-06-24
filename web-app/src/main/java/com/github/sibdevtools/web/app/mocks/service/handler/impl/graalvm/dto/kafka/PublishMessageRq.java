package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        PublishMessageRq that = (PublishMessageRq) o;
        return Arrays.equals(key, that.key) &&
                topic.equals(that.topic) &&
                Arrays.equals(value, that.value) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(groupCode, that.groupCode) &&
                Objects.equals(partition, that.partition) &&
                Objects.equals(maxTimeout, that.maxTimeout) &&
                Objects.equals(headers, that.headers) &&
                Objects.equals(bootstrapServers, that.bootstrapServers);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(groupCode);
        result = 31 * result + Objects.hashCode(bootstrapServers);
        result = 31 * result + topic.hashCode();
        result = 31 * result + Objects.hashCode(partition);
        result = 31 * result + Objects.hashCode(timestamp);
        result = 31 * result + Arrays.hashCode(key);
        result = 31 * result + Arrays.hashCode(value);
        result = 31 * result + Objects.hashCode(headers);
        result = 31 * result + Objects.hashCode(maxTimeout);
        return result;
    }
}
