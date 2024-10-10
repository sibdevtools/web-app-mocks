package com.github.sibdevtools.web.app.mocks.api.service.dto;

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
}
