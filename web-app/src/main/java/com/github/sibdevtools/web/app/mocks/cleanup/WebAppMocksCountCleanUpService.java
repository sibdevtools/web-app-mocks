package com.github.sibdevtools.web.app.mocks.cleanup;

import com.github.sibdevtools.web.app.mocks.conf.WebAppMocksCountCleanUpProperties;
import com.github.sibdevtools.web.app.mocks.event.spring.InvocationCreatedEvent;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockInvocationEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author sibmaks
 * @since 0.0.14
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@ConditionalOnProperty(name = "web.app.mocks.props.clean-up.count.enabled", havingValue = "true")
public class WebAppMocksCountCleanUpService {
    private final HttpMockInvocationEntityRepository repository;
    private final WebAppMocksCountCleanUpProperties properties;

    @TransactionalEventListener(
            classes = InvocationCreatedEvent.class,
            phase = TransactionPhase.BEFORE_COMMIT
    )
    public void execute(InvocationCreatedEvent event) {
        var mockId = event.mockId();
        var max = properties.getMax() - 1;
        var current = repository.countAllByMockId(mockId);
        if(current >= max) {
            repository.deleteAllExceptLastN(mockId, max);
        }
    }

}
