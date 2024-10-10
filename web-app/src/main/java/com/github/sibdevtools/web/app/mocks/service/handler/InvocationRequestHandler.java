package com.github.sibdevtools.web.app.mocks.service.handler;

import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockInvocationEntity;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockInvocationEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@Aspect
@Component
public class InvocationRequestHandler {
    private final HttpMockInvocationEntityRepository repository;

    @Autowired
    public InvocationRequestHandler(HttpMockInvocationEntityRepository repository) {
        this.repository = repository;
    }

    /**
     * Wrap request handles, for saving history.
     *
     * @param pjp point-cut
     * @return result of handling
     * @throws Throwable handling exception
     */
    @Around("within(com.github.sibdevtools.web.app.mocks.service.handler.RequestHandler+) && execution(* handle(..))")
    public Object handle(ProceedingJoinPoint pjp) throws Throwable {
        var args = pjp.getArgs();
        var start = System.currentTimeMillis();
        try {
            return pjp.proceed(args);
        } finally {
            var timing = System.currentTimeMillis() - start;
            var path = (String) args[0];
            var mock = (HttpMockEntity) args[1];
            var rq = (HttpServletRequest) args[2];
            var rs = (HttpServletResponse) args[3];
            var entity = HttpMockInvocationEntity.builder()
                    .mockId(mock.getId())
                    .remoteHost(rq.getRemoteHost())
                    .remoteAddress(rq.getRemoteAddr())
                    .method(rq.getMethod())
                    .path(path)
                    .timing(timing)
                    .status(rs.getStatus())
                    .bodyStorageType("TO_DELETE")
                    .bodyStorageId("TO_DELETE")
                    .createdAt(ZonedDateTime.now())
                    .build();
            repository.save(entity);
        }
    }
}
