package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibdevtools.web.app.mocks.exception.UnexpectedErrorException;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.val;
import org.graalvm.polyglot.HostAccess;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.22
 */
@Builder
public record ConsumedMessage(
        ObjectMapper objectMapper,
        @HostAccess.Export int partition,
        @HostAccess.Export long offset,
        @HostAccess.Export long timestamp,
        @HostAccess.Export String timestampType,
        @HostAccess.Export int serializedKeySize,
        @HostAccess.Export int serializedValueSize,
        @HostAccess.Export Map<String, byte[]> headers,
        @HostAccess.Export byte[] key,
        @HostAccess.Export byte[] value
) {
    /**
     * Get key as string
     *
     * @return key
     */
    @HostAccess.Export
    public String textKey() {
        return new String(key, StandardCharsets.UTF_8);
    }

    /**
     * Get key as JSON
     *
     * @return key
     */
    @HostAccess.Export
    public Object jsonKey() {
        try {
            var text = textKey();
            var jsonNode = objectMapper.readTree(text);

            if (jsonNode.isArray()) {
                return objectMapper.convertValue(jsonNode, Object[].class);
            } else if (jsonNode.isObject()) {
                return objectMapper.convertValue(jsonNode, Map.class);
            }
            return jsonNode;
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't write to response", e);
        }
    }

    /**
     * Get value as string
     *
     * @return value
     */
    @HostAccess.Export
    public String textValue() {
        return new String(value, StandardCharsets.UTF_8);
    }

    /**
     * Get value as JSON
     *
     * @return value
     */
    @HostAccess.Export
    public Object jsonValue() {
        try {
            var text = textValue();
            var jsonNode = objectMapper.readTree(text);

            if (jsonNode.isArray()) {
                return objectMapper.convertValue(jsonNode, Object[].class);
            } else if (jsonNode.isObject()) {
                return objectMapper.convertValue(jsonNode, Map.class);
            }
            return jsonNode;
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't write to response", e);
        }
    }

    /**
     * Get header value as byte array
     *
     * @param key header key
     * @return header value
     */
    @Nullable
    @HostAccess.Export
    public byte[] header(String key) {
        return headers.get(key);
    }

    /**
     * Get header value as UTF-8 string
     *
     * @param key header key
     * @return header value
     */
    @HostAccess.Export
    public String headerText(String key) {
        val value = header(key);
        if (value == null) return null;
        return new String(value, StandardCharsets.UTF_8);
    }

    /**
     * Get value as JSON
     *
     * @return value
     */
    @HostAccess.Export
    public Object headerJson(String key) {
        try {
            var text = headerText(key);
            var jsonNode = objectMapper.readTree(text);

            if (jsonNode.isArray()) {
                return objectMapper.convertValue(jsonNode, Object[].class);
            } else if (jsonNode.isObject()) {
                return objectMapper.convertValue(jsonNode, Map.class);
            }
            return jsonNode;
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't write to response", e);
        }
    }
}
