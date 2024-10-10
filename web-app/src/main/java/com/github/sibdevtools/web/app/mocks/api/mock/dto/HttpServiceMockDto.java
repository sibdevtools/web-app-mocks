package com.github.sibdevtools.web.app.mocks.api.mock.dto;

import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Builder
@AllArgsConstructor
public class HttpServiceMockDto implements Serializable {
    private long mockId;
    private String method;
    private String name;
    private String path;
    private boolean enabled;
    private String type;

    public HttpServiceMockDto(HttpMockEntity httpMockEntity) {
        this.mockId = httpMockEntity.getId();
        this.name = httpMockEntity.getName();
        this.method = httpMockEntity.getMethod();
        this.path = httpMockEntity.getPath();
        this.enabled = httpMockEntity.isEnabled();
        this.type = httpMockEntity.getType();
    }
}
