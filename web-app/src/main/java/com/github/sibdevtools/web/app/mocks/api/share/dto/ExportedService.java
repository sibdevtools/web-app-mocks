package com.github.sibdevtools.web.app.mocks.api.share.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.16
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportedService implements Serializable {
    @NotNull
    private String code;
    @NotNull
    @Size(min = 1)
    private List<ExportedMock> mocks;
}
