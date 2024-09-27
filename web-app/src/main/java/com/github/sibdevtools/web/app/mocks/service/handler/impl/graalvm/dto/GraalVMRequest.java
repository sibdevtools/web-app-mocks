package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibdevtools.web.app.mocks.exception.UnexpectedErrorException;
import jakarta.annotation.Nullable;
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
    private final ObjectMapper objectMapper;
    private final CompletableFuture<byte[]> contentFuture;

    @HostAccess.Export
    public final String remoteHost;
    @HostAccess.Export
    public final String remoteAddress;
    @HostAccess.Export
    public final String method;
    @HostAccess.Export
    public final String path;
    @HostAccess.Export
    public final Map<String, List<String>> headers;
    @HostAccess.Export
    public final Map<String, List<String>> queryParams;
    @HostAccess.Export
    public final Map<String, Cookie> cookies;

    /**
     * Construct graalvm http request
     *
     * @param objectMapper object mapper
     * @param path         request path
     * @param rq           http request
     */
    public GraalVMRequest(
            ObjectMapper objectMapper,
            String path,
            HttpServletRequest rq
    ) {
        this.objectMapper = objectMapper;
        this.remoteHost = rq.getRemoteHost();
        this.remoteAddress = rq.getRemoteAddr();
        this.method = rq.getMethod();
        this.path = path;
        this.headers = Collections.unmodifiableMap(getHeaders(rq));
        this.queryParams = Collections.unmodifiableMap(getQueryParams(rq));
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

    private static Map<String, List<String>> getHeaders(HttpServletRequest rq) {
        var headers = new HashMap<String, List<String>>();
        var names = Optional.ofNullable(rq.getHeaderNames())
                .orElseGet(Collections::emptyEnumeration);
        while (names.hasMoreElements()) {
            var key = names.nextElement();
            var values = new ArrayList<String>();
            var headerValues = rq.getHeaders(key);
            while (headerValues.hasMoreElements()) {
                var value = headerValues.nextElement();
                values.add(value);
            }
            headers.put(key, Collections.unmodifiableList(values));
        }
        return headers;
    }

    private static Map<String, List<String>> getQueryParams(HttpServletRequest rq) {
        return Optional.ofNullable(rq.getParameterMap())
                .map(Map::entrySet)
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        it -> Optional.ofNullable(it.getValue())
                                .map(Arrays::asList)
                                .orElseGet(Collections::emptyList))
                );

    }

    /**
     * Get request content body as bytes
     *
     * @return request content body
     * @throws ExecutionException   content read exception
     * @throws InterruptedException content read interrupted
     */
    @HostAccess.Export
    public byte[] bytes() throws ExecutionException, InterruptedException {
        return contentFuture.get();
    }

    /**
     * Get request content body as string
     *
     * @return request content body
     * @throws ExecutionException   content read exception
     * @throws InterruptedException content read interrupted
     */
    @HostAccess.Export
    public String text() throws ExecutionException, InterruptedException {
        return new String(contentFuture.get(), StandardCharsets.UTF_8);
    }

    /**
     * Get request content body as JSON
     *
     * @return request content body
     * @throws ExecutionException   content read exception
     * @throws InterruptedException content read interrupted
     */
    @HostAccess.Export
    public Object json() throws ExecutionException, InterruptedException {
        try {
            var text = text();
            var jsonNode = objectMapper.readTree(text);

            if (jsonNode.isArray()) {
                return objectMapper.convertValue(jsonNode, Object[].class);
            } else if (jsonNode.isObject()) {
                return objectMapper.convertValue(jsonNode, Map.class);
            }
            return jsonNode;
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't write to response", e);
        }
    }

    /**
     * Get cookie by name
     *
     * @param key cookie name
     * @return cookie
     */
    @Nullable
    @HostAccess.Export
    public Cookie cookie(String key) {
        return cookies.get(key);
    }

    /**
     * Get all header values
     *
     * @param key header key
     * @return header values as list
     */
    @Nullable
    @HostAccess.Export
    public List<String> header(String key) {
        return headers.get(key);
    }

    /**
     * Get 1st header value
     *
     * @param key header key
     * @return header value
     */
    @Nullable
    @HostAccess.Export
    public String headerFirst(String key) {
        return Optional.ofNullable(headers.get(key))
                .map(List::getFirst)
                .orElse(null);
    }

    /**
     * Get all query param values
     *
     * @param key query key
     * @return query values as list
     */
    @Nullable
    @HostAccess.Export
    public List<String> queryParam(String key) {
        return queryParams.get(key);
    }

    /**
     * Get 1st query param value
     *
     * @param key query key
     * @return query value
     */
    @Nullable
    @HostAccess.Export
    public String queryParamFirst(String key) {
        return Optional.ofNullable(queryParams.get(key))
                .map(List::getFirst)
                .orElse(null);
    }

}
