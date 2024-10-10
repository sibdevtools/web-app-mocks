package com.github.sibdevtools.web.app.mocks.service;

import com.github.sibdevtools.web.app.mocks.api.service.dto.HttpServiceDto;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpServiceMocksDto;
import com.github.sibdevtools.web.app.mocks.entity.HttpServiceEntity;
import com.github.sibdevtools.web.app.mocks.exception.ServiceNotFoundException;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockEntityRepository;
import com.github.sibdevtools.web.app.mocks.repository.HttpServiceEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@Slf4j
@Service
public class WebAppMocksServicesService {
    private final HttpServiceEntityRepository serviceEntityRepository;
    private final HttpMockEntityRepository httpMockEntityRepository;

    @Autowired
    public WebAppMocksServicesService(
            HttpServiceEntityRepository httpServiceEntityRepository,
            HttpMockEntityRepository httpMockEntityRepository
    ) {
        this.serviceEntityRepository = httpServiceEntityRepository;
        this.httpMockEntityRepository = httpMockEntityRepository;
    }

    /**
     * Create service
     *
     * @param code service code
     * @return service instance
     */
    public HttpServiceEntity create(String code) {
        var serviceEntity = HttpServiceEntity.builder()
                .code(code)
                .createdAt(ZonedDateTime.now())
                .modifiedAt(ZonedDateTime.now())
                .build();
        return serviceEntityRepository.save(serviceEntity);
    }

    /**
     * Get service info by id
     *
     * @param serviceId service identifier
     * @return service information
     */
    public HttpServiceDto getById(long serviceId) {
        var serviceEntity = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));
        return new HttpServiceDto(serviceEntity.getId(), serviceEntity.getCode());
    }

    /**
     * Get service's mocks by id
     *
     * @param serviceId service identifier
     * @return service information with mocks
     */
    public HttpServiceMocksDto getMocks(long serviceId) {
        var serviceEntity = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));
        var httpMockEntities = httpMockEntityRepository.findAllByServiceId(serviceId);
        return new HttpServiceMocksDto(
                serviceEntity,
                httpMockEntities
        );
    }

    /**
     * Get all services
     *
     * @return list of all services
     */
    public List<HttpServiceDto> getAll() {
        return serviceEntityRepository.findAll()
                .stream()
                .map(it -> new HttpServiceDto(it.getId(), it.getCode()))
                .toList();
    }

    /**
     * Delete service by id
     *
     * @param serviceId service identifier
     */
    public void deleteById(long serviceId) {
        serviceEntityRepository.deleteById(serviceId);
    }

    /**
     * Update service code
     *
     * @param id   service identifier
     * @param code service code
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void update(long id, String code) {
        serviceEntityRepository.updateCodeById(id, code, ZonedDateTime.now());
    }
}
