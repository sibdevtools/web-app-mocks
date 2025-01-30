package com.github.sibdevtools.web.app.mocks.cleanup;

import com.github.sibdevtools.async.api.rq.CreateAsyncTaskRq;
import com.github.sibdevtools.async.api.service.AsyncTaskService;
import com.github.sibdevtools.web.app.mocks.conf.WebAppMocksCountCleanUpProperties;
import com.github.sibdevtools.web.app.mocks.event.spring.InvocationCreatedEvent;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockInvocationEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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
@ConditionalOnProperty(name = "web.app.mocks.props.clean-up.count.enabled", havingValue = "true")
public class WebAppMocksCountCleanUpService {
    private final AsyncTaskService asyncTaskService;
    private final HttpMockInvocationEntityRepository repository;
    private final WebAppMocksCountCleanUpProperties properties;

    @TransactionalEventListener(
            classes = InvocationCreatedEvent.class,
            phase = TransactionPhase.BEFORE_COMMIT
    )
    public void execute(InvocationCreatedEvent event) {
        var mockId = event.mockId();
        var max = properties.getMax();
        var pageable = PageRequest.of(1, max, Sort.by(Sort.Direction.DESC, "createdAt"));
        var toDelete = repository.findAllByMockIdWithLock(mockId, pageable);
        while (toDelete.hasContent()) {
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
            toDelete = repository.findAllByMockIdWithLock(mockId, pageable);
        }
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
