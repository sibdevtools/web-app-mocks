package com.github.sibdevtools.web.app.mocks.mapper;

import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpMockInvocationItemDto;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockInvocationEntity;
import org.mapstruct.*;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@AnnotateWith(GeneratedMapper.class)
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.WARN
)
public interface HttpMockInvocationItemDtoMapper {

    @Mapping(target = "invocationId", source = "entity.id")
    HttpMockInvocationItemDto map(
            HttpMockInvocationEntity entity
    );

}
