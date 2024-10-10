package com.github.sibdevtools.web.app.mocks.conf;

import com.github.sibdevtools.error.exception.ServiceException;
import com.github.sibdevtools.storage.api.service.StorageBucketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Configuration
public class WebAppMockStartUpListener {
    private final StorageBucketService storageBucketService;

    @Value("${web.app.mocks.props.bucket.code}")
    private String bucketCode;
    @Value("${web.app.mocks.props.invocations.bucket.code}")
    private String invocationsBucketCode;

    @Autowired
    public WebAppMockStartUpListener(StorageBucketService storageBucketService) {
        this.storageBucketService = storageBucketService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        try {
            storageBucketService.create(bucketCode);
        } catch (ServiceException e) {
            log.warn("Bucket creation error: {}", e.getMessage());
        }
        try {
            storageBucketService.create(invocationsBucketCode);
        } catch (ServiceException e) {
            log.warn("Invocation bucket creation error: {}", e.getMessage());
        }
    }
}
