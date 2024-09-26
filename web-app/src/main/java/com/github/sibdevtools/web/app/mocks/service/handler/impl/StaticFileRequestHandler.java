package com.github.sibdevtools.web.app.mocks.service.handler.impl;

import com.github.sibdevtools.storage.api.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sibmaks
 * @since 0.0.8
 */
@Slf4j
@Component
public class StaticFileRequestHandler extends StaticRequestHandler {

    public StaticFileRequestHandler(StorageService storageService,
                                    CommonResponsePreparer commonResponsePreparer) {
        super(storageService, commonResponsePreparer);
    }

    @Override
    public String getType() {
        return "STATIC_FILE";
    }

}
