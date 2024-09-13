package com.github.simplemocks.web.app.mocks.service.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.simplemocks.storage.api.dto.BucketFileMetadata;
import com.github.simplemocks.storage.api.service.StorageService;
import com.github.simplemocks.web.app.mocks.entity.HttpMockEntity;
import com.github.simplemocks.web.app.mocks.service.handler.RequestHandler;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
public class StaticRequestHandler implements RequestHandler {
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    public StaticRequestHandler(StorageService storageService,
                                @Qualifier("webAppMocksObjectMapper")
                                ObjectMapper objectMapper) {
        this.storageService = storageService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getType() {
        return "STATIC";
    }

    @Override
    @SneakyThrows
    public void handle(@Nonnull String path,
                       @Nonnull HttpMockEntity httpMockEntity,
                       @Nonnull HttpServletRequest rq,
                       @Nonnull HttpServletResponse rs) {
        var storageId = httpMockEntity.getStorageId();
        var getBucketFileRs = storageService.get(storageId);
        var bucketFile = getBucketFileRs.getBody();
        var contentDescription = bucketFile.getDescription();
        var meta = contentDescription.getMeta();
        fillHeaders(rs, meta);
        var outputStream = rs.getOutputStream();
        outputStream.write(bucketFile.getData());
    }

    private void fillHeaders(HttpServletResponse rs, BucketFileMetadata meta) {
        var headersJson = meta.get("HTTP_HEADERS");
        if (headersJson != null && !headersJson.isBlank()) {
            try {
                Map<String, String> headers = objectMapper.readValue(headersJson, Map.class);
                for (var entry : headers.entrySet()) {
                    rs.setHeader(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                log.error("Can't set all http headers", e);
            }
        }

        var statusCode = meta.get("STATUS_CODE");
        if (statusCode != null) {
            var status = Integer.parseInt(statusCode);
            rs.setStatus(status);
        }
    }
}
