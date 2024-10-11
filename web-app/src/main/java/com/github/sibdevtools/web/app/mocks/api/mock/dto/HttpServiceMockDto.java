package com.github.sibdevtools.web.app.mocks.api.mock.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpServiceMockDto implements Serializable {
    private long mockId;
    private String method;
    private String name;
    private String path;
    private boolean enabled;
    private String type;
}
