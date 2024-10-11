package com.github.sibdevtools.web.app.mocks.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

public class WebAppMocksFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        if (!(req instanceof ContentCachingRequestWrapper) && req instanceof HttpServletRequest httpRequest) {
            req = new ContentCachingRequestWrapper(httpRequest);
        }

        if (!(res instanceof ContentCachingResponseWrapper) && res instanceof HttpServletResponse httpResponse) {
            res = new ContentCachingResponseWrapper(httpResponse);
        }

        super.doFilter(req, res, chain);
    }
}
