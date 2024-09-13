package com.github.simplemocks.web.app.mocks.conf;

import com.github.simplemocks.error_service.exception.ServiceException;
import com.github.simplemocks.storage.api.service.StorageBucketService;
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
    }
}
