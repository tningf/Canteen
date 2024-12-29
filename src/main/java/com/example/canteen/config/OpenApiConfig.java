package com.example.canteen.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI canteenOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Canteen API")
                        .description("API documentation for the Canteen application")
                        .version("1.0"));
    }
}