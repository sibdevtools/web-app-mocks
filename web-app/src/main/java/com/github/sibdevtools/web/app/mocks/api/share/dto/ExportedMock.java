package com.github.sibdevtools.web.app.mocks.api.share.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.16
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportedMock implements Serializable {
    @NotNull
    private String method;
    @NotNull
    private String name;
    @NotNull
    private String path;
    @NotNull
    private String type;
    private long delay;
    private boolean enabled;
    @NotNull
    private String content;
    @NotNull
    private Map<String, String> contentMetadata;
}
