package com.github.sibdevtools.web.app.mocks.service.handler.impl.js.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibdevtools.web.app.mocks.exception.UnexpectedErrorException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.graalvm.polyglot.HostAccess;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@AllArgsConstructor
public class JsResponse {
    private final ObjectMapper objectMapper;
    private final HttpServletResponse rs;

    @HostAccess.Export
    public void status(int status) {
        rs.setStatus(status);
    }

    @HostAccess.Export
    public void header(String key, String value) {
        rs.setHeader(key, value);
    }

    @HostAccess.Export
    public void plain(String body) {
        ServletOutputStream outputStream;
        try {
            outputStream = rs.getOutputStream();
            outputStream.write(body.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't write to response", e);
        }
    }

    @HostAccess.Export
    public void plain(int status, String contentType, String body) {
        status(status);
        header(HttpHeaders.CONTENT_TYPE, contentType);
        plain(body);
    }

    @HostAccess.Export
    public void bytes(byte[] bytes) {
        ServletOutputStream outputStream;
        try {
            outputStream = rs.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't write to response", e);
        }
    }

    @HostAccess.Export
    public void bytes(int status, String contentType, byte[] bytes) {
        status(status);
        header(HttpHeaders.CONTENT_TYPE, contentType);
        bytes(bytes);
    }

    @HostAccess.Export
    public void json(Object json) {
        try {
            var outputStream = rs.getOutputStream();
            objectMapper.writeValue(outputStream, json);
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't write to response", e);
        }
    }

    @HostAccess.Export
    public void json(int status, String contentType, Object json) {
        status(status);
        header(HttpHeaders.CONTENT_TYPE, contentType);
        json(json);
    }

    @HostAccess.Export
    public void cookie(String name, String value) {
        cookie(name, value, false, true);
    }

    @HostAccess.Export
    public void cookie(String name, String value, boolean secure, boolean httpOnly) {
        var cookie = new Cookie(name, value);
        cookie.setSecure(secure);
        cookie.setHttpOnly(httpOnly);
        rs.addCookie(cookie);
    }
}
