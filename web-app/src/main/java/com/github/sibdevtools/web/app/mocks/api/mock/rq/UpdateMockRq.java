package com.github.sibdevtools.web.app.mocks.api.mock.rq;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMockRq implements Serializable {
    @NotNull
    private String method;
    @NotNull
    private String name;
    @NotNull
    @Pattern(regexp = "^(?!//)(/[\\w\\p{Punct}/* \\p{L}]*)$")
    private String path;
    @NotNull
    private String type;
    @NotNull
    @Min(0)
    private Long delay;
    private Map<String, String> meta;
    private String content;
}
