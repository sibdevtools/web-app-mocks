package com.github.sibdevtools.web.app.mocks.task;

import com.github.sibdevtools.async.api.entity.AsyncTask;
import com.github.sibdevtools.async.api.rs.AsyncTaskProcessingResult;
import com.github.sibdevtools.async.api.rs.AsyncTaskProcessingResultBuilder;
import com.github.sibdevtools.async.api.service.AsyncTaskProcessor;
import com.github.sibdevtools.async.api.service.AsyncTaskProcessorMeta;
import com.github.sibdevtools.storage.api.service.StorageService;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockEntityRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author sibmaks
 * @since 0.0.18
 */
@Slf4j
@Component
@AsyncTaskProcessorMeta(
        taskType = "web-app-mocks.clean-up-removed-file",
        taskVersions = "v1"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CleanUpRemovedMockFileProcessor implements AsyncTaskProcessor {
    private final StorageService storageService;
    private final HttpMockEntityRepository httpMockEntityRepository;

    @Nonnull
    @Override
    public AsyncTaskProcessingResult process(@Nonnull AsyncTask asyncTask) {
        var parameters = asyncTask.parameters();
        var mockId = Optional.of(parameters.get("mockId"))
                .map(Long::valueOf)
                .orElseThrow(() -> new IllegalArgumentException("Missing mockId parameter"));
        var storageId = parameters.get("storageId");

        var currentFileId = httpMockEntityRepository.findById(mockId)
                .map(HttpMockEntity::getStorageId)
                .orElse(null);

        if (Objects.equals(currentFileId, storageId)) {
            log.warn("Mock {} still use file {}", mockId, storageId);
            return AsyncTaskProcessingResultBuilder.createRetryResult(
                    ZonedDateTime.now()
                            .plusMinutes(1)
            );
        }

        try {
            var rs = storageService.delete(storageId);
            if (rs.isSuccess()) {
                return AsyncTaskProcessingResultBuilder.createFinishResult();
            }
        } catch (Exception e) {
            log.error("Failed to delete file {}: {}", storageId, e.getMessage(), e);
        }
        return AsyncTaskProcessingResultBuilder.createRetryResult(
                ZonedDateTime.now()
                        .plusMinutes(1)
        );
    }
}
