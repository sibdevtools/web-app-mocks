package com.github.sibdevtools.web.app.mocks.api.mock.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpServiceMocksDto implements Serializable {
    private long serviceId;
    private String code;
    private List<HttpServiceMockDto> mocks;
}
