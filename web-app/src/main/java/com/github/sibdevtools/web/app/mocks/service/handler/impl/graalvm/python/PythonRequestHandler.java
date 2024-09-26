package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.python;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibdevtools.session.api.service.SessionService;
import com.github.sibdevtools.storage.api.service.StorageService;
import com.github.sibdevtools.web.app.mocks.service.handler.impl.CommonResponsePreparer;
import com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.GraalVMRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author sibmaks
 * @since 0.0.7
 */
@Slf4j
@Component
public class PythonRequestHandler extends GraalVMRequestHandler {

    @Autowired
    public PythonRequestHandler(StorageService storageService,
                                SessionService sessionService,
                                @Qualifier("webAppMocksObjectMapper")
                                ObjectMapper objectMapper,
                                CommonResponsePreparer commonResponsePreparer) {
        super("python", storageService, sessionService, objectMapper, commonResponsePreparer);
    }

    @Override
    public String getType() {
        return "PYTHON";
    }

}
