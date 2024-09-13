package com.github.simplemocks.web.app.mocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.github.simplemocks")
@EntityScan(basePackages = "com.github.simplemocks")
@SpringBootApplication(scanBasePackages = {
        "com.github.simplemocks.web.app.mocks.config",
        "com.github.simplemocks"
})
public class StandaloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(StandaloneApplication.class, args);
    }

}
