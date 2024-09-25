package com.github.sibdevtools.web.app.mocks.service.handler.impl.js.dto;

import lombok.Builder;
import org.graalvm.polyglot.HostAccess;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Builder
public record JSMocksContext(@HostAccess.Export JsRequest request,
                             @HostAccess.Export JsResponse response,
                             @HostAccess.Export JsSessions sessions) {
}
