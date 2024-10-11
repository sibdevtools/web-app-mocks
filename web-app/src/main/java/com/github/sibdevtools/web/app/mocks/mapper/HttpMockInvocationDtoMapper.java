package com.github.sibdevtools.web.app.mocks.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibdevtools.storage.api.dto.BucketFile;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpMockInvocationDto;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockInvocationEntity;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
public abstract class HttpMockInvocationDtoMapper {
    protected final Base64.Encoder encoder;
    @Autowired
    @Qualifier("webAppMocksObjectMapper")
    protected ObjectMapper objectMapper;

    protected HttpMockInvocationDtoMapper() {
        this.encoder = Base64.getEncoder();
    }

    @Mapping(target = "invocationId", source = "entity.id")
    @Mapping(target = "queryParams", expression = "java(MapperUtils.getQueryParams(objectMapper, rqBucketFile))")
    @Mapping(target = "rqHeaders", expression = "java(MapperUtils.getHttpHeaders(objectMapper, rqBucketFile))")
    @Mapping(target = "rqBody", expression = "java(MapperUtils.getBody(encoder, rqBucketFile))")
    @Mapping(target = "rsHeaders", expression = "java(MapperUtils.getHttpHeaders(objectMapper, rsBucketFile))")
    @Mapping(target = "rsBody", expression = "java(MapperUtils.getBody(encoder, rsBucketFile))")
    public abstract HttpMockInvocationDto map(
            HttpMockInvocationEntity entity,
            BucketFile rqBucketFile,
            BucketFile rsBucketFile
    );

}
