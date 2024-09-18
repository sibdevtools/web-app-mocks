package com.github.simplemocks.web.app.mocks.controller;

import com.github.simplemocks.common.api.rs.StandardRs;
import com.github.simplemocks.web.app.mocks.api.service.all.rs.GetServicesRs;
import com.github.simplemocks.web.app.mocks.api.service.create.rq.CreateServiceRq;
import com.github.simplemocks.web.app.mocks.api.service.create.rs.CreateServiceRs;
import com.github.simplemocks.web.app.mocks.api.service.get.rs.GetServiceRs;
import com.github.simplemocks.web.app.mocks.api.service.mocks.rs.GetServiceMocksRs;
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

    /**
     * Constructor web app mocks service controller
     *
     * @param mockService mock service
     */
    @Autowired
    public WebAppMocksServiceController(WebAppMocksService mockService) {
        this.mockService = mockService;
    }

    /**
     * Create service Rest handler
     *
     * @param rq creation request
     * @return creation response
     */
    @PostMapping(
            name = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public CreateServiceRs create(@RequestBody @Validated CreateServiceRq rq) {
        var code = rq.getCode();
        var serviceMockEntity = mockService.createService(code);
        return new CreateServiceRs(serviceMockEntity.getId());
    }

    /**
     * Update existing service Rest handler
     *
     * @param rq updating request
     * @return updating response
     */
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

    /**
     * Get service information by identifier
     *
     * @param serviceId service identifier
     * @return service information
     */
    @GetMapping("/{serviceId}")
    public GetServiceRs getById(@PathVariable("serviceId") long serviceId) {
        var service = this.mockService.getService(serviceId);
        return new GetServiceRs(service);
    }

    /**
     * Get all services
     *
     * @return all services
     */
    @GetMapping("/")
    public GetServicesRs getAll() {
        var services = mockService.getAllServices();
        return new GetServicesRs(services);
    }

    /**
     * Delete service by id
     *
     * @param serviceId service identifier
     */
    @DeleteMapping("/{serviceId}")
    public void deleteById(@PathVariable("serviceId") long serviceId) {
        this.mockService.deleteById(serviceId);
    }

    /**
     * Get all mocks for service by id
     *
     * @param serviceId service identifier
     */
    @GetMapping("/{serviceId}/mocks")
    public GetServiceMocksRs getAllMocks(@PathVariable("serviceId") long serviceId) {
        var service = this.mockService.getServiceMocks(serviceId);
        return new GetServiceMocksRs(service);
    }

}
