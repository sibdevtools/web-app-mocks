package com.github.sibdevtools.web.app.mocks.mapper;

import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpServiceMockDto;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpServiceMocksDto;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.entity.HttpServiceEntity;
import org.mapstruct.*;

import java.util.List;

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
public interface HttpServiceMocksDtoMapper {

    @Mapping(target = "serviceId", source = "entity.id")
    HttpServiceMocksDto map(
            HttpServiceEntity entity,
            List<HttpMockEntity> mocks
    );

    @Mapping(target = "mockId", source = "entity.id")
    HttpServiceMockDto map(
            HttpMockEntity entity
    );

}
