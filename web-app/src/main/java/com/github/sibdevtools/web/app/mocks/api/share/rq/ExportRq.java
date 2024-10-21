package com.github.sibdevtools.web.app.mocks.api.share.rq;

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
public class ExportRq implements Serializable {
    @NotNull
    @Size(min = 1)
    private List<Long> mocksIds;
}
