package com.github.sibdevtools.web.app.mocks.api.mock.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpMockInvocationDto implements Serializable {
    private long invocationId;
    private String remoteHost;
    private String remoteAddress;
    private String method;
    private String path;
    private Map<String, List<String>> queryParams;
    private long timing;
    private int status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSS")
    private ZonedDateTime createdAt;
    private String rqBody;
    private Map<String, List<String>> rqHeaders;
    private String rsBody;
    private Map<String, List<String>> rsHeaders;
}
