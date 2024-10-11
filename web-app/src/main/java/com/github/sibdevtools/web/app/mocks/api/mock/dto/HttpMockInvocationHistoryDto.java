package com.github.sibdevtools.web.app.mocks.api.mock.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpMockInvocationHistoryDto implements Serializable {
    private List<HttpMockInvocationItemDto> invocations;
    private long pages;
}
