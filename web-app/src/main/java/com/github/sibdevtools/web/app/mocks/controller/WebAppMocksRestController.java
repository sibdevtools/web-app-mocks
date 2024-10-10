package com.github.sibdevtools.web.app.mocks.controller;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;
import com.github.sibdevtools.common.api.rs.StandardRs;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpMockDto;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpMockInvocationDto;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpMockInvocationHistoryDto;
import com.github.sibdevtools.web.app.mocks.api.mock.rq.CreateMockRq;
import com.github.sibdevtools.web.app.mocks.api.mock.rq.SetEnabledMockRq;
import com.github.sibdevtools.web.app.mocks.api.mock.rq.UpdateMockRq;
import com.github.sibdevtools.web.app.mocks.service.WebAppMockInvocationService;
import com.github.sibdevtools.web.app.mocks.service.WebAppMocksService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final WebAppMockInvocationService webAppMockInvocationService;

    @Autowired
    public WebAppMocksRestController(WebAppMocksService webAppMocksService,
                                     WebAppMockInvocationService webAppMockInvocationService) {
        this.webAppMocksService = webAppMocksService;
        this.webAppMockInvocationService = webAppMockInvocationService;
    }

    @PostMapping(
            path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public StandardBodyRs<Long> create(
            @PathVariable("serviceId") long serviceId,
            @RequestBody @Validated CreateMockRq rq
    ) {
        var content = rq.getContent();
        var httpMockEntity = webAppMocksService.create(
                serviceId,
                rq.getMethod(),
                rq.getName(),
                rq.getPath(),
                rq.getType(),
                rq.getDelay(),
                rq.getMeta(),
                B64_DECODER.decode(content)
        );
        return new StandardBodyRs<>(httpMockEntity.getId());
    }

    @PutMapping(
            path = "/{mockId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public StandardBodyRs<Long> update(
            @PathVariable("mockId") long mockId,
            @RequestBody @Validated UpdateMockRq rq
    ) {
        var content = rq.getContent();
        var httpMockEntity = webAppMocksService.update(
                mockId,
                rq.getMethod(),
                rq.getName(),
                rq.getPath(),
                rq.getType(),
                rq.getDelay(),
                rq.getMeta(),
                B64_DECODER.decode(content)
        );
        return new StandardBodyRs<>(httpMockEntity.getId());
    }

    @PutMapping(
            path = "/{mockId}/enabled",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public StandardBodyRs<Long> setEnabled(
            @PathVariable("mockId") long mockId,
            @RequestBody SetEnabledMockRq rq
    ) {
        var httpMockEntity = webAppMocksService.setEnabled(
                mockId,
                rq.isEnabled()
        );
        return new StandardBodyRs<>(httpMockEntity.getId());
    }

    @DeleteMapping("/{mockId}")
    public StandardRs delete(@PathVariable("mockId") long mockId) {
        webAppMocksService.deleteMockById(mockId);
        return new StandardRs();
    }

    @GetMapping("/{mockId}")
    public StandardBodyRs<HttpMockDto> get(@PathVariable("mockId") long mockId) {
        var mockDto = webAppMocksService.getById(mockId);
        return new StandardBodyRs<>(mockDto);
    }

    @GetMapping("/{mockId}/url")
    public StandardBodyRs<String> getUrl(
            @PathVariable("mockId") long mockId,
            HttpServletRequest rq
    ) {
        var mockUrl = webAppMocksService.getUrl(mockId, rq);
        return new StandardBodyRs<>(mockUrl);
    }

    @GetMapping("/{mockId}/history/{pageSize}/{page}")
    public StandardBodyRs<HttpMockInvocationHistoryDto> getHistory(
            @PathVariable("mockId") long mockId,
            @PathVariable("pageSize") int pageSize,
            @PathVariable("page") int page
    ) {
        var history = webAppMockInvocationService.getHistory(mockId, page, pageSize);
        var historyDto = HttpMockInvocationHistoryDto.builder()
                .invocations(new ArrayList<>(history.getContent()))
                .pages(history.getTotalPages())
                .build();
        return new StandardBodyRs<>(historyDto);
    }

    @GetMapping("/{mockId}/history/invocation/{invocationId}")
    public StandardBodyRs<HttpMockInvocationDto> getInvocation(
            @PathVariable("mockId") long mockId,
            @PathVariable("invocationId") int invocationId
    ) {
        var invocation = webAppMockInvocationService.getInvocation(mockId, invocationId);
        return new StandardBodyRs<>(invocation);
    }

}
