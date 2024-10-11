package com.github.sibdevtools.web.app.mocks.conf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.temporal.ChronoUnit;

/**
 * @author sibmaks
 * @since 0.0.14
 */
@Setter
@Getter
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("web.app.mocks.props.clean-up.obsolete")
public class WebAppMocksObsoleteCleanUpProperties {
    private ChronoUnit ttlType = ChronoUnit.DAYS;
    private long ttl = 3;
}
