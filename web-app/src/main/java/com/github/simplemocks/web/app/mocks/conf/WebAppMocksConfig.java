package com.github.simplemocks.web.app.mocks.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.AntPathMatcher;

import javax.sql.DataSource;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Configuration
@PropertySource("classpath:web-app-mocks.properties")
public class WebAppMocksConfig {

    @Bean("webAppMocksObjectMapper")
    public ObjectMapper webAppMocksObjectMapper() {
        return JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
    }

    @Bean("webAppMocksAntPathMatcher")
    public AntPathMatcher webAppMocksAntPathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    @ConfigurationProperties("spring.flyway.web-app-mocks")
    public ClassicConfiguration webAppMocksFlywayConfiguration(DataSource dataSource) {
        var classicConfiguration = new ClassicConfiguration();
        classicConfiguration.setDataSource(dataSource);
        return classicConfiguration;
    }

    @Bean
    public Flyway webAppMocksFlyway(@Qualifier("webAppMocksFlywayConfiguration") ClassicConfiguration configuration) {
        var flyway = new Flyway(configuration);
        flyway.migrate();
        return flyway;
    }
}
