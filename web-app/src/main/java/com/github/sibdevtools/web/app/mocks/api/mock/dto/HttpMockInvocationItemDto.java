package com.github.sibdevtools.web.app.mocks.api.mock.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpMockInvocationItemDto implements Serializable {
    private long invocationId;
    private String method;
    private String path;
    private long timing;
    private int status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSS")
    private ZonedDateTime createdAt;
}
