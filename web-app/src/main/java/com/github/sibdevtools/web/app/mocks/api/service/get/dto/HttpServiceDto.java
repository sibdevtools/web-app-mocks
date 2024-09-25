package com.github.sibdevtools.web.app.mocks.api.service.get.dto;

import com.github.sibdevtools.web.app.mocks.entity.HttpServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
public class HttpServiceDto implements Serializable {
    private long serviceId;
    private String code;

    public HttpServiceDto(HttpServiceEntity serviceEntity) {
        this.serviceId = serviceEntity.getId();
        this.code = serviceEntity.getCode();
    }
}
