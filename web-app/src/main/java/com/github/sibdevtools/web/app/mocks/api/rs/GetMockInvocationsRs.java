package com.github.sibdevtools.web.app.mocks.api.rs;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;
import com.github.sibdevtools.web.app.mocks.api.dto.HttpMockInvocationHistoryDto;
import com.github.sibdevtools.web.app.mocks.api.dto.HttpMockInvocationItemDto;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

/**
 * @author sibmaks
 * @since 0.0.13
 */
public class GetMockInvocationsRs extends StandardBodyRs<HttpMockInvocationHistoryDto> {
    public GetMockInvocationsRs(Page<HttpMockInvocationItemDto> invocations) {
        super(new HttpMockInvocationHistoryDto(new ArrayList<>(invocations.getContent()), invocations.getTotalPages()));
    }
}
