package com.github.sibdevtools.web.app.mocks.service.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibdevtools.storage.api.dto.BucketFileMetadata;
import com.github.sibdevtools.web.app.mocks.constant.Constants;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.8
 */
@Slf4j
@Component
public class CommonResponsePreparer {
    private final ObjectMapper objectMapper;

    public CommonResponsePreparer(@Qualifier("webAppMocksObjectMapper")
                                  ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Prepare http response
     *
     * @param rs   response
     * @param meta mock meta info
     */
    public void prepare(HttpServletResponse rs, BucketFileMetadata meta) {
        prepareHttpHeaders(rs, meta);

        prepareStatusCode(rs, meta);
    }

    private static void prepareStatusCode(HttpServletResponse rs, BucketFileMetadata meta) {
        var statusCode = meta.get(Constants.META_STATUS_CODE);
        if (statusCode != null) {
            var status = Integer.parseInt(statusCode);
            rs.setStatus(status);
        }
    }

    private void prepareHttpHeaders(HttpServletResponse rs, BucketFileMetadata meta) {
        var headersJson = meta.get(Constants.META_HTTP_HEADERS);
        if (headersJson == null || headersJson.isBlank()) {
            return;
        }
        try {
            Map<String, String> headers = objectMapper.readValue(headersJson, Map.class);
            for (var entry : headers.entrySet()) {
                rs.setHeader(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            log.error("Can't set all http headers", e);
        }
    }
}
