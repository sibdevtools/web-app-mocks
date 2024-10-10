package com.github.sibdevtools.web.app.mocks.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpUtils {

    /**
     * Get request body as byte array
     *
     * @param rq http request
     * @return request body as byte array
     * @throws IOException if request body cannot be read
     */
    public static byte[] getRqBody(HttpServletRequest rq) throws IOException {
        var rqInputStream = rq.getInputStream();
        if (rqInputStream == null) {
            return new byte[0];
        }
        byte[] rqBody = rqInputStream.readAllBytes();
        if (rqBody == null || rqBody.length == 0) {
            return new byte[0];
        }
        return rqBody;
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
