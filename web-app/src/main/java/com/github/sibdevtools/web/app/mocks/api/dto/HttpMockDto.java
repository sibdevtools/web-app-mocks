package com.github.sibdevtools.web.app.mocks.api.dto;

import com.github.sibdevtools.storage.api.dto.BucketFile;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
public class HttpMockDto implements Serializable {
    private long serviceId;
    private long mockId;
    private String method;
    private String name;
    private String path;
    private String type;
    private Map<String, String> meta;
    private String content;

    public HttpMockDto(@Nonnull HttpMockEntity httpMockEntity,
                       @Nonnull BucketFile bucketFile) {
        var service = httpMockEntity.getService();
        this.serviceId = service.getId();
        this.mockId = httpMockEntity.getId();
        this.method = httpMockEntity.getMethod();
        this.name = httpMockEntity.getName();
        this.path = httpMockEntity.getPath();
        this.type = httpMockEntity.getType();
        this.meta = getMeta(bucketFile);
        this.content = Base64.getEncoder()
                .encodeToString(bucketFile.getData());
    }

    private static Map<String, String> getMeta(@Nonnull BucketFile bucketFile) {
        var description = bucketFile.getDescription();
        var descriptionMeta = description.getMeta();
        var attributeNames = descriptionMeta.getAttributeNames();
        return attributeNames.stream()
                .collect(Collectors.toMap(it -> it, descriptionMeta::get));

    }
}
