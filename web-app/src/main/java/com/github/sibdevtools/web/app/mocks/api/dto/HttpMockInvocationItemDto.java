package com.github.sibdevtools.web.app.mocks.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockInvocationEntity;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@Getter
@AllArgsConstructor
public class HttpMockInvocationItemDto implements Serializable {
    private long invocationId;
    private String method;
    private String path;
    private long timing;
    private int status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private ZonedDateTime createdAt;

    public HttpMockInvocationItemDto(@Nonnull HttpMockInvocationEntity invocationEntity) {
        this.invocationId = invocationEntity.getId();
        this.method = invocationEntity.getMethod();
        this.path = invocationEntity.getPath();
        this.timing = invocationEntity.getTiming();
        this.status = invocationEntity.getStatus();
        this.createdAt = invocationEntity.getCreatedAt();
    }

}
