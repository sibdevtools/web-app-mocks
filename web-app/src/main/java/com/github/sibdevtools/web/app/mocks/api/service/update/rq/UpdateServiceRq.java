package com.github.sibdevtools.web.app.mocks.api.service.update.rq;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceRq implements Serializable {
    @Min(1)
    private long serviceId;
    @NotNull
    @Size(min = 1, max = 128)
    private String code;
}
