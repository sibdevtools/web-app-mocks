package com.github.simplemocks.web.app.mocks.controller;

import com.github.simplemocks.common.api.rs.StandardRs;
import com.github.simplemocks.web.app.mocks.api.rq.CreateMockRq;
import com.github.simplemocks.web.app.mocks.api.rq.SetEnabledMockRq;
import com.github.simplemocks.web.app.mocks.api.rq.UpdateMockRq;
import com.github.simplemocks.web.app.mocks.api.rs.CreateMockRs;
import com.github.simplemocks.web.app.mocks.api.rs.GetMockRs;
import com.github.simplemocks.web.app.mocks.api.rs.UpdateMockRs;
import com.github.simplemocks.web.app.mocks.service.WebAppMocksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping(
        path = "${web.app.mocks.uri.rest.mocks.path}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class WebAppMocksRestController {
    private static final Base64.Decoder B64_DECODER = Base64.getDecoder();

    private final WebAppMocksService webAppMocksService;

    @Autowired
    public WebAppMocksRestController(WebAppMocksService webAppMocksService) {
        this.webAppMocksService = webAppMocksService;
    }

    @PostMapping(
            path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public CreateMockRs create(@PathVariable("serviceId") long serviceId,
                               @RequestBody CreateMockRq rq) {
        var content = rq.getContent();
        var httpMockEntity = webAppMocksService.create(
                serviceId,
                rq.getMethod(),
                rq.getName(),
                rq.getAntPattern(),
                rq.getType(),
                rq.getMeta(),
                B64_DECODER.decode(content)
        );
        return new CreateMockRs(httpMockEntity.getId());
    }

    @PutMapping(
            path = "/{mockId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public UpdateMockRs update(@PathVariable("mockId") long mockId, @RequestBody UpdateMockRq rq) {
        var content = rq.getContent();
        var httpMockEntity = webAppMocksService.update(
                mockId,
                rq.getMethod(),
                rq.getName(),
                rq.getAntPattern(),
                rq.getType(),
                rq.getMeta(),
                B64_DECODER.decode(content)
        );
        return new UpdateMockRs(httpMockEntity.getId());
    }

    @PutMapping(
            path = "/{mockId}/enabled",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public UpdateMockRs setEnabled(@PathVariable("mockId") long mockId, @RequestBody SetEnabledMockRq rq) {
        var httpMockEntity = webAppMocksService.setEnabled(
                mockId,
                rq.isEnabled()
        );
        return new UpdateMockRs(httpMockEntity.getId());
    }

    @DeleteMapping("/{mockId}")
    public StandardRs delete(@PathVariable("mockId") long mockId) {
        webAppMocksService.deleteMockById(mockId);
        return new StandardRs();
    }

    @GetMapping("/{mockId}")
    public GetMockRs get(@PathVariable("mockId") long mockId) {
        var mockDto = webAppMocksService.get(mockId);
        return new GetMockRs(mockDto);
    }

}
