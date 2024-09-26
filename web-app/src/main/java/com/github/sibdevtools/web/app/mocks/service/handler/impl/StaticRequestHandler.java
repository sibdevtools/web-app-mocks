package com.github.sibdevtools.web.app.mocks.service.handler.impl;

import com.github.sibdevtools.storage.api.service.StorageService;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.service.handler.RequestHandler;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
public class StaticRequestHandler implements RequestHandler {
    private final StorageService storageService;
    private final CommonResponsePreparer commonResponsePreparer;

    public StaticRequestHandler(StorageService storageService,
                                CommonResponsePreparer commonResponsePreparer) {
        this.storageService = storageService;
        this.commonResponsePreparer = commonResponsePreparer;
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
        commonResponsePreparer.prepare(rs, meta);
        var outputStream = rs.getOutputStream();
        outputStream.write(bucketFile.getData());
    }

}
