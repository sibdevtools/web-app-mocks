package com.github.sibdevtools.web.app.mocks.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@Getter
@AllArgsConstructor
public class HttpMockInvocationHistoryDto implements Serializable {
    private List<HttpMockInvocationItemDto> invocations;
    private long pages;
}
