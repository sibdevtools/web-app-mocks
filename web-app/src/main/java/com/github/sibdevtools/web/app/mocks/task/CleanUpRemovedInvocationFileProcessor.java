package com.github.sibdevtools.web.app.mocks.task;

import com.github.sibdevtools.async.api.entity.AsyncTask;
import com.github.sibdevtools.async.api.rs.AsyncTaskProcessingResult;
import com.github.sibdevtools.async.api.rs.AsyncTaskProcessingResultBuilder;
import com.github.sibdevtools.async.api.service.AsyncTaskProcessor;
import com.github.sibdevtools.async.api.service.AsyncTaskProcessorMeta;
import com.github.sibdevtools.storage.api.service.StorageService;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockInvocationEntityRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author sibmaks
 * @since 0.0.18
 */
@Slf4j
@Component
@AsyncTaskProcessorMeta(
        taskType = "web-app-mocks.remove-invocation-file",
        taskVersions = "v1"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CleanUpRemovedInvocationFileProcessor implements AsyncTaskProcessor {
    private final StorageService storageService;
    private final HttpMockInvocationEntityRepository repository;

    @Nonnull
    @Override
    public AsyncTaskProcessingResult process(@Nonnull AsyncTask asyncTask) {
        var parameters = asyncTask.parameters();
        var invocationId = Optional.of(parameters.get("invocationId"))
                .map(Long::valueOf)
                .orElseThrow(() -> new IllegalArgumentException("Missing invocationId parameter"));
        var storageId = parameters.get("storageId");

        var invocationEntity = repository.findById(invocationId)
                .orElse(null);

        if (invocationEntity != null) {
            log.warn("Invocation {} still use file {}", invocationId, storageId);
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
