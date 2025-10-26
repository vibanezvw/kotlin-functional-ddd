package com.digitalhub.examples.infrastructure.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("Kotlin Functional DDD API")
                .version("1.0.0")
                .description("API para gestión de usuarios utilizando DDD, CQRS y Railway-Oriented Programming")
                .contact(
                    Contact()
                        .name("Digital Hub")
                        .email("info@digitalhub.com")
                )
        )
}
