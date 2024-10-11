package com.github.sibdevtools.web.app.mocks.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.sibdevtools.web.app.mocks.service.WebAppMockInvocationService;
import com.github.sibdevtools.web.app.mocks.service.handler.InvocationRequestHandler;
import com.github.sibdevtools.web.app.mocks.service.handler.RequestHandler;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.AntPathMatcher;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Configuration
@EnableAspectJAutoProxy
@PropertySource("classpath:web/app/mocks/application.properties")
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

    @Bean("webAppMocksInvocationRequestHandler")
    @ConditionalOnProperty(name = "web.app.mocks.props.invocation.history.enabled", havingValue = "true")
    public InvocationRequestHandler webAppMocksInvocationRequestHandler(
            WebAppMockInvocationService invocationService
    ) {
        return new InvocationRequestHandler(invocationService);
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

    @Bean("webAppMocksHandlerTypeToHandler")
    public Set<String> webAppMocksHandlerTypes(List<RequestHandler> handlers) {
        return handlers.stream()
                .map(RequestHandler::getType)
                .collect(Collectors.toSet());
    }

    @Bean
    public TaskScheduler webAppMocksScheduledExecutor() {
        return new ThreadPoolTaskSchedulerBuilder()
                .threadNamePrefix("webAppMocks-")
                .poolSize(Runtime.getRuntime().availableProcessors())
                .build();
    }
}
