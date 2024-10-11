package com.github.sibdevtools.web.app.mocks.service.handler;

import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.service.WebAppMockInvocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@Aspect
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvocationRequestHandler {
    private final WebAppMockInvocationService invocationService;

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

            invocationService.save(
                    timing,
                    path,
                    mock,
                    rq,
                    rs
            );
        }
    }
}
