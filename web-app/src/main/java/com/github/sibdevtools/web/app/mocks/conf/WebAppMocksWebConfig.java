package com.github.sibdevtools.web.app.mocks.conf;

import com.github.sibdevtools.web.app.mocks.filter.WebAppMocksFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Configuration
@EnableWebMvc
public class WebAppMocksWebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/web/app/mocks/ui/**")
                .addResourceLocations("classpath:/web/app/mocks/static/")
                .resourceChain(true)
                .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/web/app/mocks/ui/")
                .setViewName("forward:/web/app/mocks/ui/index.html");
        registry.addViewController("/web/app/mocks/ui/service/**")
                .setViewName("forward:/web/app/mocks/ui/index.html");
    }

    @Bean
    public FilterRegistrationBean<WebAppMocksFilter> webAppMocksFilter(
            @Value("${web.app.mocks.uri.mock.path.ant}")
            String path
    ) {
        var registrationBean = new FilterRegistrationBean<WebAppMocksFilter>();

        registrationBean.setFilter(new WebAppMocksFilter());
        registrationBean.addUrlPatterns(path);
        registrationBean.setOrder(Integer.MIN_VALUE);

        return registrationBean;
    }
}
