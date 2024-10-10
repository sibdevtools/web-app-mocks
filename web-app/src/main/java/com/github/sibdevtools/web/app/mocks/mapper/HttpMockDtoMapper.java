package com.github.sibdevtools.web.app.mocks.mapper;

import com.github.sibdevtools.storage.api.dto.BucketFile;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpMockDto;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import org.mapstruct.*;

import java.util.Base64;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@AnnotateWith(GeneratedMapper.class)
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.WARN,
        imports = {
                MapperUtils.class
        }
)
public abstract class HttpMockDtoMapper {
    protected final Base64.Encoder encoder;

    protected HttpMockDtoMapper() {
        this.encoder = Base64.getEncoder();
    }

    @Mapping(target = "mockId", source = "entity.id")
    @Mapping(target = "serviceId", source = "entity.service.id")
    @Mapping(target = "meta", expression = "java(MapperUtils.getMeta(bucketFile))")
    @Mapping(target = "content", expression = "java(encoder.encodeToString(bucketFile.getData()))")
    public abstract HttpMockDto map(
            HttpMockEntity entity,
            BucketFile bucketFile
    );

}
