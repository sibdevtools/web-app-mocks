package com.github.sibdevtools.web.app.mocks.service;

import com.github.sibdevtools.storage.api.service.StorageService;
import com.github.sibdevtools.web.app.mocks.api.share.dto.Exported;
import com.github.sibdevtools.web.app.mocks.api.share.dto.ExportedMock;
import com.github.sibdevtools.web.app.mocks.api.share.dto.ExportedService;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.exception.MockNotFoundException;
import com.github.sibdevtools.web.app.mocks.exception.UnexpectedErrorException;
import com.github.sibdevtools.web.app.mocks.mapper.MapperUtils;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebAppMocksShareService {
    private final HttpMockEntityRepository httpMockEntityRepository;
    private final StorageService storageService;
    private final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * Export selected mocks
     *
     * @param mocksIds exported mocks ids
     * @return exported mocks
     */
    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.REPEATABLE_READ
    )
    public Exported export(List<Long> mocksIds) {
        var services = new ArrayList<ExportedService>();

        var found = httpMockEntityRepository.findAllById(mocksIds);
        if (found.size() != mocksIds.size()) {
            var foundIds = found.stream()
                    .map(HttpMockEntity::getId)
                    .collect(Collectors.toSet());
            var notFound = mocksIds.stream()
                    .filter(it -> !foundIds.contains(it))
                    .findFirst()
                    .orElseThrow(() -> new UnexpectedErrorException("Not found absent mocks"));
            throw new MockNotFoundException(notFound);
        }

        var serviceToMocks = found.stream()
                .collect(Collectors.groupingBy(HttpMockEntity::getService));

        for (var entry : serviceToMocks.entrySet()) {
            var exportedMocks = new ArrayList<ExportedMock>();

            var mocks = entry.getValue();
            for (var mock : mocks) {
                var storageId = mock.getStorageId();
                var bucketFile = storageService.get(storageId)
                        .getBody();

                var data = bucketFile.getData();
                var exportedMockData = encoder.encodeToString(data);

                var contentMetadata = MapperUtils.getMeta(bucketFile);

                var exportedMock = ExportedMock.builder()
                        .method(mock.getMethod())
                        .name(mock.getName())
                        .path(mock.getPath())
                        .type(mock.getType())
                        .delay(mock.getDelay())
                        .enabled(mock.isEnabled())
                        .content(exportedMockData)
                        .contentMetadata(contentMetadata)
                        .build();
                exportedMocks.add(exportedMock);
            }

            var service = ExportedService.builder()
                    .code(entry.getKey().getCode())
                    .mocks(exportedMocks)
                    .build();
            services.add(service);
        }

        return Exported.builder()
                .version("v1")
                .services(services)
                .createdAt(ZonedDateTime.now())
                .build();
    }
}
