package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.HostAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sibmaks
 * @since 0.0.24
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebApplicationMocksGraalVMUtils {
    private final WebApplicationMocksGraalVMUtilsBase64 base64;
    private final WebApplicationMocksGraalVMUtilsBinary binary;
    private final WebApplicationMocksGraalVMUtilsJson json;

    @HostAccess.Export
    public WebApplicationMocksGraalVMUtilsBase64 base64() {
        return base64;
    }

    @HostAccess.Export
    public WebApplicationMocksGraalVMUtilsBinary binary() {
        return binary;
    }

    @HostAccess.Export
    public WebApplicationMocksGraalVMUtilsJson json() {
        return json;
    }


}
