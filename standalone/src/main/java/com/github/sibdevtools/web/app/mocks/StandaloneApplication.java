package com.github.sibdevtools.web.app.mocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.github.sibdevtools")
@EntityScan(basePackages = "com.github.sibdevtools")
@SpringBootApplication(scanBasePackages = {
        "com.github.sibdevtools.web.app.mocks.config",
        "com.github.sibdevtools"
})
public class StandaloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(StandaloneApplication.class, args);
    }

}
