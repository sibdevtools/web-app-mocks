package com.github.simplemocks.web.app.mocks.controller;

import com.github.simplemocks.web.app.mocks.api.rq.CreateServiceRq;
import com.github.simplemocks.web.app.mocks.api.rq.GetServiceRq;
import com.github.simplemocks.web.app.mocks.api.rs.CreateServiceRs;
import com.github.simplemocks.web.app.mocks.api.rs.GetServiceRs;
import com.github.simplemocks.web.app.mocks.api.rs.GetServicesRs;
import com.github.simplemocks.web.app.mocks.service.WebAppMockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(
        value = "${web.app.mocks.uri.rest.services.path}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class WebAppMocksServiceController {
    private final WebAppMockService mockService;

    @Autowired
    public WebAppMocksServiceController(WebAppMockService mockService) {
        this.mockService = mockService;
    }

    @PostMapping("/create")
    public CreateServiceRs create(@RequestBody CreateServiceRq rq) {
        var code = rq.getCode();
        var serviceMockEntity = mockService.createService(code);
        return new CreateServiceRs(serviceMockEntity.getId());
    }

    @PostMapping("/get")
    public GetServiceRs get(@RequestBody GetServiceRq rq) {
        var serviceId = rq.getServiceId();
        var service = this.mockService.getService(serviceId);
        return new GetServiceRs(service);
    }

    @GetMapping("/getAll")
    public GetServicesRs getAll() {
        var services = mockService.getAllServices();
        return new GetServicesRs(services);
    }

}
