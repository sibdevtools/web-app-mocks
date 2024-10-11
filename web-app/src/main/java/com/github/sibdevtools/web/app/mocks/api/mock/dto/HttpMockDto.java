package com.github.sibdevtools.web.app.mocks.api.mock.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpMockDto implements Serializable {
    private long serviceId;
    private long mockId;
    private String method;
    private String name;
    private String path;
    private String type;
    private long delay;
    private Map<String, String> meta;
    private String content;
}
