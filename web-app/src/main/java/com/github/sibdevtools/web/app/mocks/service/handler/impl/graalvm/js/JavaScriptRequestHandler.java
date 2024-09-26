package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.js;

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
 * @since 0.0.1
 */
@Slf4j
@Component
public class JavaScriptRequestHandler extends GraalVMRequestHandler {

    @Autowired
    public JavaScriptRequestHandler(StorageService storageService,
                                    SessionService sessionService,
                                    @Qualifier("webAppMocksObjectMapper")
                                    ObjectMapper objectMapper,
                                    CommonResponsePreparer commonResponsePreparer) {
        super("js", storageService, sessionService, objectMapper, commonResponsePreparer);
    }

    @Override
    public String getType() {
        return "JS";
    }

}
