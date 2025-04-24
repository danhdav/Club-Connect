package com.clubConnect.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Club Connect API")
                        .version("1.0.0")
                        .description("API documentation for Club Connect application")
                        .contact(new Contact()
                                .name("Club Connect Team")
                                .email("team@clubconnect.example.com"))
                        .license(new License()
                                .name("MIT License")));
    }
} 