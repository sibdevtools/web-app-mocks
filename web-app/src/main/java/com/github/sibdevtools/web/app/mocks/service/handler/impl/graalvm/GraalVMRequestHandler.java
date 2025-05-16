package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibdevtools.storage.api.service.StorageService;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.service.handler.RequestHandler;
import com.github.sibdevtools.web.app.mocks.service.handler.impl.CommonResponsePreparer;
import com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.*;
import com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka.WebApplicationMocksGraalVMKafka;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;

import java.nio.charset.StandardCharsets;

/**
 * @author sibmaks
 * @since 0.0.7
 */
@Slf4j
@AllArgsConstructor
public abstract class GraalVMRequestHandler implements RequestHandler {
    protected final String language;
    protected final StorageService storageService;
    protected final WebApplicationMocksGraalVMSessions graalVMSessions;
    protected final WebApplicationMocksGraalVMKafka graalVMKafka;
    protected final ObjectMapper objectMapper;
    protected final CommonResponsePreparer commonResponsePreparer;

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

        var context = GraalVMMocksContext.builder()
                .request(new GraalVMRequest(objectMapper, path, rq))
                .response(new GraalVMResponse(objectMapper, rs))
                .sessions(graalVMSessions)
                .kafka(graalVMKafka)
                .build();

        try (var js = Context.newBuilder(language)
                .allowHostAccess(HostAccess.ALL)
                .build()) {
            js.getBindings(language).putMember("wam", context);
            var script = new String(bucketFile.getData(), StandardCharsets.UTF_8);
            try {
                js.eval(language, script);
            } catch (Exception e) {
                log.error("Template execution exception", e);
            }
        }
    }

}
