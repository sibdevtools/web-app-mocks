package com.github.sibdevtools.web.app.mocks.service;

import com.github.sibdevtools.storage.api.rq.SaveFileRq;
import com.github.sibdevtools.storage.api.service.StorageService;
import com.github.sibdevtools.web.app.mocks.api.dto.HttpMockDto;
import com.github.sibdevtools.web.app.mocks.api.service.get.dto.HttpServiceDto;
import com.github.sibdevtools.web.app.mocks.api.service.mocks.dto.HttpServiceItemDto;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.entity.HttpServiceEntity;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockEntityRepository;
import com.github.sibdevtools.web.app.mocks.repository.HttpServiceEntityRepository;
import com.github.sibdevtools.web.app.mocks.service.handler.RequestHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Service
public class WebAppMocksService {
    private final Set<String> mockTypes;
    private final HttpServiceEntityRepository serviceEntityRepository;
    private final HttpMockEntityRepository httpMockEntityRepository;
    private final StorageService storageService;

    @Value("${web.app.mocks.props.bucket.code}")
    private String bucketCode;

    @Value("${web.app.mocks.uri.mock.path}")
    private String mockUriPath;

    @Autowired
    public WebAppMocksService(List<RequestHandler> handlers,
                              HttpServiceEntityRepository httpServiceEntityRepository,
                              HttpMockEntityRepository httpMockEntityRepository,
                              StorageService storageService) {
        this.mockTypes = handlers.stream()
                .map(RequestHandler::getType)
                .collect(Collectors.toSet());
        this.serviceEntityRepository = httpServiceEntityRepository;
        this.httpMockEntityRepository = httpMockEntityRepository;
        this.storageService = storageService;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HttpMockEntity create(long serviceId,
                                 String method,
                                 String name,
                                 String path,
                                 String type,
                                 Map<String, String> meta,
                                 byte[] content) {
        if (!mockTypes.contains(type)) {
            throw new IllegalArgumentException("Type %s not supported".formatted(type));
        }
        var serviceEntity = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service %s not found".formatted(serviceId)));
        meta = meta == null ? Collections.emptyMap() : meta;
        var contentName = "%s-%s-%s".formatted(method, serviceEntity.getCode(), type);

        var saveRq = SaveFileRq.builder()
                .bucket(bucketCode)
                .name(contentName)
                .meta(meta)
                .data(content)
                .build();
        var saveFileRs = storageService.save(saveRq);
        var contentId = saveFileRs.getBody();

        var httpMockEntity = HttpMockEntity.builder()
                .method(method)
                .name(name)
                .path(path)
                .service(serviceEntity)
                .type(type)
                .enabled(true)
                .storageType("LOCAL")
                .storageId(contentId)
                .createdAt(ZonedDateTime.now())
                .modifiedAt(ZonedDateTime.now())
                .build();
        return httpMockEntityRepository.save(httpMockEntity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HttpMockEntity update(long mockId,
                                 String method,
                                 String name,
                                 String path,
                                 String type,
                                 Map<String, String> meta,
                                 byte[] content) {
        if (!mockTypes.contains(type)) {
            throw new IllegalArgumentException("Type %s not supported".formatted(type));
        }
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new IllegalArgumentException("Mock %s not found".formatted(mockId)));
        meta = meta == null ? Collections.emptyMap() : meta;
        var serviceEntity = httpMockEntity.getService();
        var contentName = "%s-%s-%s".formatted(method, serviceEntity.getCode(), type);

        var saveRq = SaveFileRq.builder()
                .bucket(bucketCode)
                .name(contentName)
                .meta(meta)
                .data(content)
                .build();
        var saveFileRs = storageService.save(saveRq);
        var contentId = saveFileRs.getBody();

        httpMockEntity.setMethod(method);
        httpMockEntity.setName(name);
        httpMockEntity.setPath(path);
        httpMockEntity.setType(type);
        httpMockEntity.setStorageType("LOCAL");
        httpMockEntity.setStorageId(contentId);
        httpMockEntity.setModifiedAt(ZonedDateTime.now());

        return httpMockEntityRepository.save(httpMockEntity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HttpMockEntity setEnabled(long mockId,
                                     boolean enabled) {
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new IllegalArgumentException("Mock %s not found".formatted(mockId)));

        httpMockEntity.setEnabled(enabled);
        httpMockEntity.setModifiedAt(ZonedDateTime.now());

        return httpMockEntityRepository.save(httpMockEntity);
    }

    public HttpMockDto get(long mockId) {
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new IllegalArgumentException("Mock %s not found".formatted(mockId)));
        var storageId = httpMockEntity.getStorageId();
        var getBucketFileRs = storageService.get(storageId);
        var bucketFile = getBucketFileRs.getBody();
        return new HttpMockDto(httpMockEntity, bucketFile);
    }

    public String getUrl(long mockId,
                         HttpServletRequest rq) {
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new IllegalArgumentException("Mock %s not found".formatted(mockId)));

        var service = httpMockEntity.getService();

        var path = httpMockEntity.getPath();

        var scheme = rq.getScheme();
        var serverName = rq.getServerName();
        var serverPort = rq.getServerPort();
        var contextPath = rq.getContextPath();

        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(serverName)
                .port(serverPort)
                .path(contextPath)
                .path(mockUriPath)
                .path(service.getCode())
                .path(path)
                .toUriString();
    }

    /**
     * Create service
     *
     * @param code service code
     * @return service instance
     */
    public HttpServiceEntity createService(String code) {
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
    public HttpServiceDto getService(long serviceId) {
        var serviceEntity = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service %s not found".formatted(serviceId)));
        return new HttpServiceDto(serviceEntity);
    }

    /**
     * Get service's mocks by id
     *
     * @param serviceId service identifier
     * @return service information with mocks
     */
    public HttpServiceItemDto getServiceMocks(long serviceId) {
        var serviceEntity = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service %s not found".formatted(serviceId)));
        var httpMockEntities = httpMockEntityRepository.findAllByServiceId(serviceId);
        return new HttpServiceItemDto(
                serviceEntity,
                httpMockEntities
        );
    }

    /**
     * Get all services
     *
     * @return list of all services
     */
    public List<com.github.sibdevtools.web.app.mocks.api.service.all.dto.HttpServiceDto> getAllServices() {
        return serviceEntityRepository.findAll()
                .stream()
                .map(com.github.sibdevtools.web.app.mocks.api.service.all.dto.HttpServiceDto::new)
                .toList();
    }

    /**
     * Delete service by id
     *
     * @param serviceId service identifier
     */
    public void deleteServiceById(long serviceId) {
        serviceEntityRepository.deleteById(serviceId);
    }

    /**
     * Update service code
     *
     * @param id   service identifier
     * @param code service code
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateService(long id, String code) {
        serviceEntityRepository.updateCodeById(id, code, ZonedDateTime.now());
    }

    /**
     * Delete mock by id
     *
     * @param mockId mock identifier
     */
    public void deleteMockById(long mockId) {
        httpMockEntityRepository.deleteById(mockId);
    }
}
