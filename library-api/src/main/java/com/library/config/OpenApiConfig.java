package com.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI / Swagger documentation.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates the OpenAPI specification bean for the Library Management API.
     *
     * @return the configured OpenAPI instance
     */
    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management API")
                        .version("v1.0")
                        .description("REST API for Library Management System"));
    }
}
