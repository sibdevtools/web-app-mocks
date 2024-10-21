package com.github.sibdevtools.web.app.mocks.api.share.rq;

import com.github.sibdevtools.web.app.mocks.api.share.dto.ExportedService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportRq implements Serializable {
    @NotNull
    @Size(min = 1)
    private List<ExportedService> services;
}
