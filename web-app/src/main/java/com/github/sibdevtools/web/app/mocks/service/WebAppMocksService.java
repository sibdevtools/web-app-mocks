package com.github.sibdevtools.web.app.mocks.service;

import com.github.sibdevtools.async.api.rq.CreateAsyncTaskRq;
import com.github.sibdevtools.async.api.service.AsyncTaskService;
import com.github.sibdevtools.storage.api.dto.BucketFile;
import com.github.sibdevtools.storage.api.rq.SaveFileRq;
import com.github.sibdevtools.storage.api.service.StorageService;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpMockDto;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.exception.MockNotFoundException;
import com.github.sibdevtools.web.app.mocks.exception.ServiceNotFoundException;
import com.github.sibdevtools.web.app.mocks.mapper.HttpMockDtoMapper;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockEntityRepository;
import com.github.sibdevtools.web.app.mocks.repository.HttpServiceEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebAppMocksService {
    private final Set<String> webAppMocksHandlerTypes;
    private final HttpServiceEntityRepository serviceEntityRepository;
    private final HttpMockEntityRepository httpMockEntityRepository;
    private final StorageService storageService;
    private final HttpMockDtoMapper httpMockDtoMapper;
    private final AsyncTaskService asyncTaskService;

    @Value("${web.app.mocks.props.bucket.code}")
    private String bucketCode;

    @Value("${web.app.mocks.uri.mock.path}")
    private String mockUriPath;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HttpMockEntity create(long serviceId,
                                 String method,
                                 String name,
                                 String path,
                                 String type,
                                 long delay,
                                 boolean enabled,
                                 Map<String, String> meta,
                                 byte[] content) {
        if (!webAppMocksHandlerTypes.contains(type)) {
            throw new IllegalArgumentException("Type %s not supported".formatted(type));
        }
        var serviceEntity = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));
        meta = meta == null ? Collections.emptyMap() : meta;
        var contentName = "%s-%s-%s".formatted(method, serviceEntity.getCode(), type);

        var contentId = saveFile(meta, content, contentName);

        var now = ZonedDateTime.now();
        var httpMockEntity = HttpMockEntity.builder()
                .method(method)
                .name(name)
                .path(path)
                .service(serviceEntity)
                .type(type)
                .delay(delay)
                .enabled(enabled)
                .storageType("LOCAL")
                .storageId(contentId)
                .createdAt(now)
                .modifiedAt(now)
                .build();
        return httpMockEntityRepository.save(httpMockEntity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HttpMockEntity update(long mockId,
                                 String method,
                                 String name,
                                 String path,
                                 String type,
                                 long delay,
                                 Map<String, String> meta,
                                 byte[] content) {
        if (!webAppMocksHandlerTypes.contains(type)) {
            throw new IllegalArgumentException("Type %s not supported".formatted(type));
        }
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new MockNotFoundException(mockId));
        meta = meta == null ? Collections.emptyMap() : meta;
        var serviceEntity = httpMockEntity.getService();
        var contentName = "%s-%s-%s".formatted(method, serviceEntity.getCode(), type);

        registerCleanUpTask(mockId, httpMockEntity);

        var contentId = saveFile(meta, content, contentName);

        httpMockEntity.setMethod(method);
        httpMockEntity.setName(name);
        httpMockEntity.setPath(path);
        httpMockEntity.setType(type);
        httpMockEntity.setDelay(delay);
        httpMockEntity.setStorageType("LOCAL");
        httpMockEntity.setStorageId(contentId);
        httpMockEntity.setModifiedAt(ZonedDateTime.now());

        return httpMockEntityRepository.save(httpMockEntity);
    }

    private String saveFile(Map<String, String> meta, byte[] content, String contentName) {
        var saveRq = SaveFileRq.builder()
                .bucket(bucketCode)
                .name(contentName)
                .meta(meta)
                .data(content)
                .build();
        var saveFileRs = storageService.save(saveRq);
        return saveFileRs.getBody();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HttpMockEntity setEnabled(long mockId,
                                     boolean enabled) {
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new MockNotFoundException(mockId));

        httpMockEntity.setEnabled(enabled);
        httpMockEntity.setModifiedAt(ZonedDateTime.now());

        return httpMockEntityRepository.save(httpMockEntity);
    }

    /**
     * Get mock by ID
     *
     * @param mockId mock identifier
     * @return mock instance
     */
    public HttpMockDto getById(long mockId) {
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new MockNotFoundException(mockId));
        var storageId = httpMockEntity.getStorageId();
        var bucketFile = getBucketFile(storageId);
        return httpMockDtoMapper.map(httpMockEntity, bucketFile);
    }

    /**
     * Get mock URL
     *
     * @param mockId mock identifier
     * @param rq     http request
     * @return mock URL
     */
    public String getUrl(long mockId,
                         HttpServletRequest rq) {
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new MockNotFoundException(mockId));

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
     * Delete mock by id
     *
     * @param mockId mock identifier
     */
    public void deleteMockById(long mockId) {
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElse(null);

        if (httpMockEntity == null) {
            return;
        }

        registerCleanUpTask(mockId, httpMockEntity);

        httpMockEntityRepository.deleteById(mockId);
    }

    private void registerCleanUpTask(long mockId, HttpMockEntity httpMockEntity) {
        var cleanUpTaskRs = asyncTaskService.registerTask(
                CreateAsyncTaskRq.builder()
                        .uid(UUID.randomUUID().toString())
                        .type("web-app-mocks.clean-up-removed-file")
                        .version("v1")
                        .scheduledStartTime(ZonedDateTime.now().plusSeconds(10))
                        .parameters(Map.ofEntries(
                                Map.entry("mockId", Long.toString(mockId)),
                                Map.entry("storageId", httpMockEntity.getStorageId())
                        ))
                        .build()
        );
        if (!cleanUpTaskRs.isSuccess() || !cleanUpTaskRs.getBody()) {
            throw new RuntimeException("Failed to schedule cleanup task");
        }
    }

    private BucketFile getBucketFile(String storageId) {
        if (storageId == null) {
            return null;
        }
        var getBucketFileRs = storageService.get(storageId);
        return getBucketFileRs.getBody();
    }
}
