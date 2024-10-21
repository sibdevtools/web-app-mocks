package com.github.sibdevtools.web.app.mocks.controller;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;
import com.github.sibdevtools.web.app.mocks.api.share.dto.Exported;
import com.github.sibdevtools.web.app.mocks.api.share.rq.ExportRq;
import com.github.sibdevtools.web.app.mocks.service.WebAppMocksShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(
        value = "${web.app.mocks.uri.rest.share.path}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebAppMocksShareController {
    private final WebAppMocksShareService shareService;

    /**
     * Export mocks Rest handler
     *
     * @param rq export request
     * @return export response
     */
    @PostMapping(
            path = "/v1/export",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public StandardBodyRs<Exported> create(
            @RequestBody @Validated ExportRq rq
    ) {
        var mocksIds = rq.getMocksIds();
        var exported = shareService.export(mocksIds);
        return new StandardBodyRs<>(exported);
    }

}
