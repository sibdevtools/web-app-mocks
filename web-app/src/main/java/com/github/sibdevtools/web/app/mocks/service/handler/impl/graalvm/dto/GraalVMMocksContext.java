package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto;

import com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka.WebApplicationMocksGraalVMKafka;
import com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kvs.WebApplicationMocksGraalVMKeyValueStorage;
import com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.utils.WebApplicationMocksGraalVMUtils;
import lombok.Builder;
import org.graalvm.polyglot.HostAccess;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Builder
public record GraalVMMocksContext(
        @HostAccess.Export GraalVMRequest request,
        @HostAccess.Export GraalVMResponse response,
        @HostAccess.Export WebApplicationMocksGraalVMSessions sessions,
        @HostAccess.Export WebApplicationMocksGraalVMKafka kafka,
        @HostAccess.Export WebApplicationMocksGraalVMKeyValueStorage keyValueStorage,
        @HostAccess.Export WebApplicationMocksGraalVMUtils utils
) {
}
