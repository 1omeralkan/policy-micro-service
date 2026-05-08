package com.omeralkan.policymicroservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Policy Microservice API")
                        .version("1.0.0")
                        .description("Poliçe işlemlerinin yönetildiği kurumsal mikroservis API dökümantasyonu"));
    }

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (operation, handlerMethod) -> {
            Parameter acceptLanguageHeader = new Parameter()
                    .in("header")
                    .schema(new io.swagger.v3.oas.models.media.StringSchema()._default("tr"))
                    .name("Accept-Language")
                    .description("Dil seçimi (tr veya en)")
                    .required(false);

            operation.addParametersItem(acceptLanguageHeader);
            return operation;
        };
    }
}