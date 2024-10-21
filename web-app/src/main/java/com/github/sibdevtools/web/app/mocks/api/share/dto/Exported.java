package com.github.sibdevtools.web.app.mocks.api.share.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
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
public class Exported implements Serializable {
    private String version;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSS")
    private ZonedDateTime createdAt;
    private List<ExportedService> services;
}
