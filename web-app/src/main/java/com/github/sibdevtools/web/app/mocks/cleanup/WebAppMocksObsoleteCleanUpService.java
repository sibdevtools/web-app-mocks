package com.github.sibdevtools.web.app.mocks.cleanup;

import com.github.sibdevtools.web.app.mocks.conf.WebAppMocksObsoleteCleanUpProperties;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockInvocationEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * @author sibmaks
 * @since 0.0.14
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@ConditionalOnProperty(name = "web.app.mocks.props.clean-up.obsolete.enabled", havingValue = "true")
public class WebAppMocksObsoleteCleanUpService {
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
        var deleted = repository.deleteAllByCreatedAtBefore(bound);
        if (deleted > 0) {
            log.info("Deleted {} obsolete mocks invocations", deleted);
        }
    }

}
