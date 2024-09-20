package com.github.simplemocks.web.app.mocks.service;

import com.github.simplemocks.web.app.mocks.exception.NotFoundException;
import com.github.simplemocks.web.app.mocks.repository.HttpMockEntityRepository;
import com.github.simplemocks.web.app.mocks.service.handler.RequestHandler;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequestDispatcher {
    private final Map<String, RequestHandler> handlers;
    private final HttpMockEntityRepository httpMockEntityRepository;
    private final AntPathMatcher antPathMatcher;

    @Autowired
    public RequestDispatcher(List<RequestHandler> handlers,
                             HttpMockEntityRepository httpMockEntityRepository,
                             @Qualifier("webAppMocksAntPathMatcher")
                             AntPathMatcher antPathMatcher) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(RequestHandler::getType, it -> it));
        this.httpMockEntityRepository = httpMockEntityRepository;
        this.antPathMatcher = antPathMatcher;
    }

    /**
     * Dispatch incoming request to service
     *
     * @param method      http method code
     * @param serviceCode http service code
     * @param path        request path
     * @param rq          incoming request
     * @param rs          outgoing response
     */
    public void dispatch(@Nonnull String method,
                         @Nonnull String serviceCode,
                         @Nonnull String path,
                         @Nonnull HttpServletRequest rq,
                         @Nonnull HttpServletResponse rs) {
        var httpMocks = httpMockEntityRepository.findAllByMethodAndServiceCodeAndEnabledIsTrue(method, serviceCode);
        for (var httpMock : httpMocks) {
            var mockPath = httpMock.getPath();
            if (!mockPath.equals(path) || !antPathMatcher.match(mockPath, path)) {
                continue;
            }
            var type = httpMock.getType();
            var requestHandler = handlers.get(type);
            if (requestHandler == null) {
                throw new NotFoundException("Request handler with type %s not exists".formatted(type));
            }
            requestHandler.handle(path, httpMock, rq, rs);
            return;
        }
        throw new NotFoundException("Handler for %s %s %s not found".formatted(method, serviceCode, path));
    }

}
