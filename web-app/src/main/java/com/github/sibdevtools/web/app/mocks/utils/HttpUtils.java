package com.github.sibdevtools.web.app.mocks.utils;

import com.github.sibdevtools.web.app.mocks.exception.UnexpectedErrorException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpUtils {

    /**
     * Get request body as byte array
     *
     * @param rq http request
     * @return request body as byte array
     * @throws UnexpectedErrorException if request body cannot be read
     */
    public static byte[] getRqBody(HttpServletRequest rq) {
        try {
            var rqInputStream = rq.getInputStream();
            if (rqInputStream == null) {
                return new byte[0];
            }
            byte[] rqBody = rqInputStream.readAllBytes();
            if (rqBody == null || rqBody.length == 0) {
                return new byte[0];
            }
            return rqBody;
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't read request body", e);
        }
    }

    /**
     * Get response body as byte array
     *
     * @param rs http response
     * @return response body as byte array
     * @throws UnexpectedErrorException if response body cannot be read
     */
    public static byte[] getRsBody(ServletResponse rs) {
        while (rs instanceof HttpServletResponseWrapper httpServletResponseWrapper && !(rs instanceof ContentCachingResponseWrapper)) {
            rs = httpServletResponseWrapper.getResponse();
        }
        if (!(rs instanceof ContentCachingResponseWrapper cachingResponseWrapper)) {
            log.error("Unsupported response type: {}", rs.getClass().getCanonicalName());
            return new byte[0];
        }
        var body = cachingResponseWrapper.getContentAsByteArray();
        try {
            cachingResponseWrapper.copyBodyToResponse();
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't store response", e);
        }
        return body;
    }

    /**
     * Get request headers
     *
     * @param rq http request
     * @return request headers
     */
    public static Map<String, List<String>> getHeaders(HttpServletRequest rq) {
        var headers = new HashMap<String, List<String>>();
        var headerNames = rq.getHeaderNames();
        if (headerNames == null) {
            return Collections.emptyMap();
        }
        while (headerNames.hasMoreElements()) {
            var header = headerNames.nextElement();
            headers.put(header, getHeaderValues(rq, header));
        }
        return headers;
    }

    private static List<String> getHeaderValues(HttpServletRequest rq, String headerName) {
        var enumeration = rq.getHeaders(headerName);
        var values = new ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            var value = enumeration.nextElement();
            values.add(value);
        }
        return values;
    }

    /**
     * Get response headers
     *
     * @param rs http response
     * @return response headers
     */
    public static Map<String, List<String>> getHeaders(HttpServletResponse rs) {
        var headers = new HashMap<String, List<String>>();
        var headerNames = rs.getHeaderNames();
        if (headerNames == null) {
            return Collections.emptyMap();
        }
        for (var header : headerNames) {
            headers.put(header, getHeaderValues(rs, header));
        }
        return headers;
    }

    private static List<String> getHeaderValues(HttpServletResponse rs, String headerName) {
        return Optional.ofNullable(rs.getHeaders(headerName))
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }

    /**
     * Get request query parameters
     *
     * @param rq http request
     * @return query parameters
     */
    public static Map<String, List<String>> getQueryParams(HttpServletRequest rq) {
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
}
