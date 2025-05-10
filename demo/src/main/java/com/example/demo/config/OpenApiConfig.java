package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI translationServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Translation Microservice API")
                        .description("Spring Boot microservice that provides translation capabilities using Kafka for asynchronous processing")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("API Support")
                                .url("https://github.com/yourusername/translation-service")
                                .email("support@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development Server")
                ));
    }
}