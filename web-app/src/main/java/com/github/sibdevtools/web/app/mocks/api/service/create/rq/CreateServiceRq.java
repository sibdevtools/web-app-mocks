package com.github.sibdevtools.web.app.mocks.api.service.create.rq;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class CreateServiceRq implements Serializable {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9\\-.]{1,128}$")
    private String code;
}
