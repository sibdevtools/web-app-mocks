package com.github.sibdevtools.web.app.mocks;

import com.github.sibdevtools.localization.api.dto.LocalizationId;
import com.github.sibdevtools.localization.api.dto.LocalizationSourceId;
import com.github.sibdevtools.localization.mutable.api.source.LocalizationJsonSource;
import com.github.sibdevtools.webapp.api.dto.HealthStatus;
import com.github.sibdevtools.webapp.api.dto.WebApplication;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Component
@LocalizationJsonSource(
        systemCode = "WEB.APP.MOCKS",
        path = "classpath:/web/app/mocks/content/localizations/eng.json",
        iso3Code = "eng"
)
@LocalizationJsonSource(
        systemCode = "WEB.APP.MOCKS",
        path = "classpath:/web/app/mocks/content/localizations/rus.json",
        iso3Code = "rus"
)
public class WebAppMocks implements WebApplication {
    private static final LocalizationSourceId LOCALIZATION_SOURCE_ID = new LocalizationSourceId("WEB.APP.MOCKS");

    @Nonnull
    @Override
    public String getCode() {
        return "web.app.mocks";
    }

    @Nonnull
    @Override
    public String getFrontendUrl() {
        return "/web/app/mocks/ui/";
    }

    @Nonnull
    @Override
    public LocalizationId getIconCode() {
        return new LocalizationId(LOCALIZATION_SOURCE_ID, "web.app.mocks.icon");
    }

    @Nonnull
    @Override
    public LocalizationId getTitleCode() {
        return new LocalizationId(LOCALIZATION_SOURCE_ID, "web.app.mocks.title");
    }

    @Nonnull
    @Override
    public LocalizationId getDescriptionCode() {
        return new LocalizationId(LOCALIZATION_SOURCE_ID, "web.app.mocks.description");
    }

    @Nonnull
    @Override
    public Set<String> getTags() {
        return Set.of(
                "http",
                "mocks"
        );
    }

    @Nonnull
    @Override
    public HealthStatus getHealthStatus() {
        return HealthStatus.UP;
    }
}
