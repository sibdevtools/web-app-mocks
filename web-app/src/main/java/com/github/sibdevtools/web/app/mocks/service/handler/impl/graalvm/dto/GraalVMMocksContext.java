package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto;

import lombok.Builder;
import org.graalvm.polyglot.HostAccess;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Builder
public record GraalVMMocksContext(@HostAccess.Export GraalVMRequest request,
                                  @HostAccess.Export GraalVMResponse response,
                                  @HostAccess.Export GraalVMSessions sessions) {
}
