package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto;

import com.github.sibdevtools.web.app.mocks.exception.UnexpectedErrorException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.graalvm.polyglot.HostAccess;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GraalVMRequest {
    @HostAccess.Export
    public final String method;
    @HostAccess.Export
    public final String path;
    @HostAccess.Export
    public final Map<String, String> headers;
    private final CompletableFuture<byte[]> contentFuture;
    private final Map<String, Cookie> cookies;

    public GraalVMRequest(String path, HttpServletRequest rq) {
        this.method = rq.getMethod();
        this.path = path;
        this.headers = Collections.unmodifiableMap(getHeaders(rq));
        this.contentFuture = getContentFuture(rq);
        this.cookies = Optional.ofNullable(rq.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .collect(Collectors.toMap(Cookie::getName, Function.identity()));
    }

    private static CompletableFuture<byte[]> getContentFuture(HttpServletRequest rq) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var inputStream = rq.getInputStream();
                return inputStream.readAllBytes();
            } catch (IOException e) {
                throw new UnexpectedErrorException("Can't read rq content", e);
            }
        });
    }

    private static Map<String, String> getHeaders(HttpServletRequest rq) {
        var headers = new HashMap<String, String>();
        var headerNames = Optional.ofNullable(rq.getHeaderNames())
                .orElseGet(Collections::emptyEnumeration);
        while (headerNames.hasMoreElements()) {
            var headerKey = headerNames.nextElement();
            var headerValue = rq.getHeader(headerKey);
            headers.put(headerKey, headerValue);
        }
        return headers;
    }

    @HostAccess.Export
    public byte[] bytes() throws ExecutionException, InterruptedException {
        return contentFuture.get();
    }

    @HostAccess.Export
    public String text() throws ExecutionException, InterruptedException {
        return new String(contentFuture.get(), StandardCharsets.UTF_8);
    }

    @HostAccess.Export
    public Cookie cookie(String key) {
        return cookies.get(key);
    }

}
