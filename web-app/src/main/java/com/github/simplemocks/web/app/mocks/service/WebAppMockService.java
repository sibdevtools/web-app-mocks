package com.github.simplemocks.web.app.mocks.service;

import com.github.simplemocks.storage.api.rq.SaveFileRq;
import com.github.simplemocks.storage.api.service.StorageService;
import com.github.simplemocks.web.app.mocks.api.dto.HttpMockDto;
import com.github.simplemocks.web.app.mocks.api.dto.HttpServiceDto;
import com.github.simplemocks.web.app.mocks.entity.HttpMockEntity;
import com.github.simplemocks.web.app.mocks.entity.HttpServiceEntity;
import com.github.simplemocks.web.app.mocks.repository.HttpMockEntityRepository;
import com.github.simplemocks.web.app.mocks.repository.HttpServiceEntityRepository;
import com.github.simplemocks.web.app.mocks.service.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Service
public class WebAppMockService {
    private final Set<String> mockTypes;
    private final HttpServiceEntityRepository serviceEntityRepository;
    private final HttpMockEntityRepository httpMockEntityRepository;
    private final StorageService storageService;
    private final AntPathMatcher antPathMatcher;

    @Value("${web.app.mocks.props.bucket.code}")
    private String bucketCode;

    @Autowired
    public WebAppMockService(List<RequestHandler> handlers,
                             HttpServiceEntityRepository httpServiceEntityRepository,
                             HttpMockEntityRepository httpMockEntityRepository,
                             StorageService storageService,
                             @Qualifier("webAppMocksAntPathMatcher")
                             AntPathMatcher antPathMatcher) {
        this.mockTypes = handlers.stream()
                .map(RequestHandler::getType)
                .collect(Collectors.toSet());
        this.serviceEntityRepository = httpServiceEntityRepository;
        this.httpMockEntityRepository = httpMockEntityRepository;
        this.storageService = storageService;
        this.antPathMatcher = antPathMatcher;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HttpMockEntity create(long serviceId,
                                 String method,
                                 String antPattern,
                                 String type,
                                 Map<String, String> meta,
                                 byte[] content) {
        if (!mockTypes.contains(type)) {
            throw new IllegalArgumentException("Type %s not supported".formatted(type));
        }
        if (!antPathMatcher.isPattern(antPattern)) {
            throw new IllegalArgumentException("Not valid ant path passed: '%s'".formatted(antPattern));
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
                .antPattern(antPattern)
                .service(serviceEntity)
                .type(type)
                .storageType("LOCAL")
                .storageId(contentId)
                .createdAt(new Date())
                .build();
        return httpMockEntityRepository.save(httpMockEntity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HttpMockEntity update(long mockId,
                                 String method,
                                 String antPattern,
                                 String type,
                                 Map<String, String> meta,
                                 byte[] content) {
        if (!mockTypes.contains(type)) {
            throw new IllegalArgumentException("Type %s not supported".formatted(type));
        }
        if (!antPathMatcher.isPattern(antPattern)) {
            throw new IllegalArgumentException("Not valid ant path passed: '%s'".formatted(antPattern));
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
        httpMockEntity.setAntPattern(antPattern);
        httpMockEntity.setType(type);
        httpMockEntity.setStorageType("LOCAL");
        httpMockEntity.setStorageId(contentId);

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

    public HttpServiceEntity createService(String code) {
        var serviceEntity = HttpServiceEntity.builder()
                .code(code)
                .createdAt(new Date())
                .build();
        return serviceEntityRepository.save(serviceEntity);
    }

    public HttpServiceDto getService(long serviceId) {
        var serviceEntity = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service %s not found".formatted(serviceId)));
        var httpMockEntities = httpMockEntityRepository.findAllByServiceId(serviceId);
        return new HttpServiceDto(serviceEntity, httpMockEntities);
    }

    public List<HttpServiceDto> getAllServices() {
        return serviceEntityRepository.findAll()
                .stream()
                .map(it -> {
                    var mocks = httpMockEntityRepository.findAllByServiceId(it.getId());
                    return new HttpServiceDto(it, mocks);
                })
                .toList();
    }
}
