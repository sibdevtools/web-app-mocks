package com.github.sibdevtools.web.app.mocks.cleanup;

import com.github.sibdevtools.async.api.rq.CreateAsyncTaskRq;
import com.github.sibdevtools.async.api.service.AsyncTaskService;
import com.github.sibdevtools.web.app.mocks.conf.WebAppMocksObsoleteCleanUpProperties;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockInvocationEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * @author sibmaks
 * @since 0.0.14
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@ConditionalOnProperty(name = "web.app.mocks.props.clean-up.obsolete.enabled", havingValue = "true")
public class WebAppMocksObsoleteCleanUpService {
    private final AsyncTaskService asyncTaskService;
    private final HttpMockInvocationEntityRepository repository;
    private final WebAppMocksObsoleteCleanUpProperties properties;

    @Transactional(
            isolation = Isolation.REPEATABLE_READ,
            propagation = Propagation.REQUIRES_NEW
    )
    @Scheduled(cron = "${web.app.mocks.props.clean-up.obsolete.cron}", scheduler = "webAppMocksScheduledExecutor")
    public void execute() {
        var ttl = properties.getTtl();
        var ttlType = properties.getTtlType();
        var bound = ZonedDateTime.now()
                .minus(ttl, ttlType);
        var maxDeleteBatch = properties.getMaxDeleteBatch();
        var deleted = 0;
        do {
            var toDelete = repository.findAllByCreatedAtBefore(bound, Pageable.ofSize(maxDeleteBatch));
            deleted = toDelete.getNumberOfElements();
            if (!toDelete.isEmpty()) {
                log.info("Delete {} obsolete mocks invocations", deleted);
            }

            for (var entity : toDelete) {
                var rqBodyStorageId = entity.getRqBodyStorageId();
                if (rqBodyStorageId != null) {
                    registerCleanUpTask(
                            entity.getId(),
                            rqBodyStorageId
                    );
                }
                var rsBodyStorageId = entity.getRsBodyStorageId();
                if (rsBodyStorageId != null) {
                    registerCleanUpTask(
                            entity.getId(),
                            rsBodyStorageId
                    );
                }
            }

            repository.deleteAll(toDelete);
        } while (deleted > 0);
    }

    private void registerCleanUpTask(long invocationId, String storageId) {
        var cleanUpTaskRs = asyncTaskService.registerTask(
                CreateAsyncTaskRq.builder()
                        .uid(UUID.randomUUID().toString())
                        .type("web-app-mocks.remove-invocation-file")
                        .version("v1")
                        .scheduledStartTime(ZonedDateTime.now().plusSeconds(10))
                        .parameters(Map.ofEntries(
                                Map.entry("invocationId", Long.toString(invocationId)),
                                Map.entry("storageId", storageId)
                        ))
                        .build()
        );
        if (!cleanUpTaskRs.isSuccess() || !cleanUpTaskRs.getBody()) {
            throw new RuntimeException("Failed to schedule cleanup task");
        }
    }
}
