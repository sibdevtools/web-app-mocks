package com.github.simplemocks.web.app.mocks.service.handler;

import com.github.simplemocks.web.app.mocks.entity.HttpMockEntity;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Request handler interface.
 *
 * @author sibmaks
 * @version 0.0.1
 */
public interface RequestHandler {

    /**
     * Unique type of request handler.
     *
     * @return unique type
     */
    String getType();

    /**
     * Handle incoming request.
     *
     * @param path           request path
     * @param httpMockEntity http mock entity
     * @param rq             incoming request
     * @param rs             outgoing response
     */
    void handle(@Nonnull String path,
                @Nonnull HttpMockEntity httpMockEntity,
                @Nonnull HttpServletRequest rq,
                @Nonnull HttpServletResponse rs);
}
