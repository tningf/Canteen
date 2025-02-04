package com.example.canteen.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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