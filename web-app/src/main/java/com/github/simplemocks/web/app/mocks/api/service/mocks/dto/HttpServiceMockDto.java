package com.github.simplemocks.web.app.mocks.api.service.mocks.dto;

import com.github.simplemocks.web.app.mocks.entity.HttpMockEntity;
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
    private String antPattern;
    private String type;

    public HttpServiceMockDto(HttpMockEntity httpMockEntity) {
        this.mockId = httpMockEntity.getId();
        this.method = httpMockEntity.getMethod();
        this.antPattern = httpMockEntity.getAntPattern();
        this.type = httpMockEntity.getType();
    }
}
