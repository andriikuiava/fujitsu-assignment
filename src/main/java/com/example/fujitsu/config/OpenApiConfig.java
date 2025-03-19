package com.example.fujitsu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * Configures OpenAPI documentation for the application.
     *
     * @return Configured OpenAPI object with application information
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery Fee API")
                        .description("API for calculating delivery fees based on weather conditions")
                        .version("1.0.0"));
    }
}
