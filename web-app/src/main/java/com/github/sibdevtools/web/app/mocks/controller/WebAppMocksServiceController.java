package com.github.sibdevtools.web.app.mocks.controller;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpServiceMocksDto;
import com.github.sibdevtools.web.app.mocks.api.service.dto.HttpServiceDto;
import com.github.sibdevtools.web.app.mocks.api.service.rq.CreateServiceRq;
import com.github.sibdevtools.web.app.mocks.api.service.rq.UpdateServiceRq;
import com.github.sibdevtools.web.app.mocks.service.WebAppMocksServicesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(
        value = "${web.app.mocks.uri.rest.services.path}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class WebAppMocksServiceController {
    private final WebAppMocksServicesService servicesService;

    /**
     * Constructor web app mocks service controller
     *
     * @param servicesService service's service
     */
    @Autowired
    public WebAppMocksServiceController(WebAppMocksServicesService servicesService) {
        this.servicesService = servicesService;
    }

    /**
     * Create service Rest handler
     *
     * @param rq creation request
     * @return creation response
     */
    @PostMapping(
            path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public StandardBodyRs<Long> create(
            @RequestBody @Validated CreateServiceRq rq
    ) {
        var code = rq.getCode();
        var serviceMockEntity = servicesService.create(code);
        return new StandardBodyRs<>(serviceMockEntity.getId());
    }

    /**
     * Update existing service Rest handler
     *
     * @param rq updating request
     * @return updating response
     */
    @PutMapping(
            path = "/{serviceId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public StandardBodyRs<Long> update(
            @PathVariable("serviceId") long serviceId,
            @RequestBody @Validated UpdateServiceRq rq
    ) {
        var code = rq.getCode();
        servicesService.update(serviceId, code);
        return new StandardBodyRs<>(serviceId);
    }

    /**
     * Get service information by identifier
     *
     * @param serviceId service identifier
     * @return service information
     */
    @GetMapping("/{serviceId}")
    public StandardBodyRs<HttpServiceDto> getById(
            @PathVariable("serviceId") long serviceId
    ) {
        var service = this.servicesService.getById(serviceId);
        return new StandardBodyRs<>(service);
    }

    /**
     * Get all services
     *
     * @return all services
     */
    @GetMapping("/")
    public StandardBodyRs<ArrayList<HttpServiceDto>> getAll() {
        var services = Optional.ofNullable(servicesService.getAll())
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
        return new StandardBodyRs<>(services);
    }

    /**
     * Delete service by id
     *
     * @param serviceId service identifier
     */
    @DeleteMapping("/{serviceId}")
    public void deleteById(@PathVariable("serviceId") long serviceId) {
        servicesService.deleteById(serviceId);
    }

    /**
     * Get all mocks for service by id
     *
     * @param serviceId service identifier
     */
    @GetMapping("/{serviceId}/mocks")
    public StandardBodyRs<HttpServiceMocksDto> getAllMocks(@PathVariable("serviceId") long serviceId) {
        var service = servicesService.getMocks(serviceId);
        return new StandardBodyRs<>(service);
    }

}
