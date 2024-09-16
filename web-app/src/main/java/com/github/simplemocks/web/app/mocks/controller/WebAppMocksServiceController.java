package com.github.simplemocks.web.app.mocks.controller;

import com.github.simplemocks.common.api.rs.StandardRs;
import com.github.simplemocks.web.app.mocks.api.rs.GetServiceRs;
import com.github.simplemocks.web.app.mocks.api.service.create.rq.CreateServiceRq;
import com.github.simplemocks.web.app.mocks.api.service.create.rs.CreateServiceRs;
import com.github.simplemocks.web.app.mocks.api.service.get.rs.GetServicesRs;
import com.github.simplemocks.web.app.mocks.api.service.update.rq.UpdateServiceRq;
import com.github.simplemocks.web.app.mocks.service.WebAppMocksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(
        value = "${web.app.mocks.uri.rest.services.path}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class WebAppMocksServiceController {
    private final WebAppMocksService mockService;

    @Autowired
    public WebAppMocksServiceController(WebAppMocksService mockService) {
        this.mockService = mockService;
    }

    @PostMapping(
            name = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public CreateServiceRs create(@RequestBody @Validated CreateServiceRq rq) {
        var code = rq.getCode();
        var serviceMockEntity = mockService.createService(code);
        return new CreateServiceRs(serviceMockEntity.getId());
    }

    @PutMapping(
            name = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public StandardRs update(@RequestBody @Validated UpdateServiceRq rq) {
        var id = rq.getServiceId();
        var code = rq.getCode();
        mockService.updateService(id, code);
        return new StandardRs();
    }

    @GetMapping("/{serviceId}")
    public GetServiceRs getById(@PathVariable("serviceId") long serviceId) {
        var service = this.mockService.getService(serviceId);
        return new GetServiceRs(service);
    }

    @GetMapping("/")
    public GetServicesRs getAll() {
        var services = mockService.getAllServices();
        return new GetServicesRs(services);
    }

    @DeleteMapping("/{serviceId}")
    public void deleteById(@PathVariable("serviceId") long serviceId) {
        this.mockService.deleteById(serviceId);
    }

}
