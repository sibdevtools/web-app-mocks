package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.python;

import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.service.handler.RequestHandler;
import com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.GraalVMRequestHandler;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sibmaks
 * @since 0.0.7
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PythonRequestHandler implements RequestHandler {
    private final GraalVMRequestHandler graalVMRequestHandler;

    @Override
    public void handle(
            @Nonnull String path,
            @Nonnull HttpMockEntity httpMockEntity,
            @Nonnull HttpServletRequest rq,
            @Nonnull HttpServletResponse rs
    ) {
        graalVMRequestHandler.handle(
                "python",
                path,
                httpMockEntity,
                rq,
                rs
        );
    }

    @Override
    public String getType() {
        return "PYTHON";
    }

}
