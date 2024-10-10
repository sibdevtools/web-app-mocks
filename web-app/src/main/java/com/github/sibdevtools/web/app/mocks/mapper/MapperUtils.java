package com.github.sibdevtools.web.app.mocks.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibdevtools.storage.api.dto.BucketFile;
import com.github.sibdevtools.storage.api.dto.BucketFileDescription;
import com.github.sibdevtools.web.app.mocks.constant.Constants;
import com.github.sibdevtools.web.app.mocks.exception.UnexpectedErrorException;
import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MapperUtils {

    /**
     * Get file meta as map
     *
     * @param bucketFile file
     * @return meta as maps
     */
    public static Map<String, String> getMeta(@Nonnull BucketFile bucketFile) {
        var description = bucketFile.getDescription();
        var descriptionMeta = description.getMeta();
        var attributeNames = descriptionMeta.getAttributeNames();
        return attributeNames.stream()
                .collect(Collectors.toMap(it -> it, descriptionMeta::get));
    }

    /**
     * Get query params from file
     *
     * @param objectMapper mapper
     * @param bucketFile   file
     * @return query params
     */
    public static Map getQueryParams(
            ObjectMapper objectMapper,
            BucketFile bucketFile
    ) {
        return getMap(bucketFile, Constants.META_QUERY_PARAMS, objectMapper);
    }

    /**
     * Get http headers from file
     *
     * @param objectMapper mapper
     * @param bucketFile   file
     * @return http headers
     */
    public static Map getHttpHeaders(
            ObjectMapper objectMapper,
            BucketFile bucketFile
    ) {
        return getMap(bucketFile, Constants.META_HTTP_HEADERS, objectMapper);
    }

    /**
     * Get bucket file data
     *
     * @param encoder    encoder
     * @param bucketFile file
     * @return http headers
     */
    public static String getBody(
            Base64.Encoder encoder,
            BucketFile bucketFile
    ) {
        return Optional.ofNullable(bucketFile)
                .map(BucketFile::getData)
                .filter(it -> it.length > 0)
                .map(encoder::encodeToString)
                .orElse(null);
    }

    private static Map getMap(BucketFile bucketFile,
                              String metaQueryParams,
                              ObjectMapper objectMapper) {
        return Optional.ofNullable(bucketFile)
                .map(BucketFile::getDescription)
                .map(BucketFileDescription::getMeta)
                .map(it -> it.get(metaQueryParams))
                .map(it -> {
                    try {
                        return objectMapper.readValue(it, Map.class);
                    } catch (JsonProcessingException e) {
                        throw new UnexpectedErrorException("Can't parse query params", e);
                    }
                })
                .orElse(null);
    }
}
