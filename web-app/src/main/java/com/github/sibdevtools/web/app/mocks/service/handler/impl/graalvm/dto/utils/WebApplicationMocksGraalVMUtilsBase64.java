package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.utils;

import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.HostAccess;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * @author sibmaks
 * @since 0.0.24
 */
@Slf4j
@Component
public class WebApplicationMocksGraalVMUtilsBase64 {
    private final Base64.Decoder decoder = Base64.getDecoder();
    private final Base64.Encoder encoder = Base64.getEncoder();

    @HostAccess.Export
    public byte[] decode(String value) {
        if (value == null) {
            return null;
        }
        return decoder.decode(value);
    }

    @HostAccess.Export
    public byte[] decode(byte[] value) {
        if (value == null) {
            return null;
        }
        return decoder.decode(value);
    }

    @HostAccess.Export
    public byte[] encode(byte[] value) {
        if (value == null) {
            return null;
        }
        return encoder.encode(value);
    }

    @HostAccess.Export
    public String encodeToString(byte[] value) {
        if (value == null) {
            return null;
        }
        return encoder.encodeToString(value);
    }
}
