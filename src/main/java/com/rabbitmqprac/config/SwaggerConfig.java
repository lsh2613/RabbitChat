package com.rabbitmqprac.config;

import com.rabbitmqprac.global.util.ApiExceptionExplainParser;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.method.HandlerMethod;

import static org.springframework.security.config.Elements.JWT;

@OpenAPIDefinition(
        servers = {
                @Server(url = "${rabbit.server.domain.local}", description = "Local Server"),
                @Server(url = "${rabbit.server.domain.dev}", description = "Develop Server")
        }
)
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    private final Environment environment;

    @Bean
    public OpenAPI openAPI() {
        String[] profiles = environment.getActiveProfiles();
        String activeProfile = (profiles.length > 0) ? profiles[0] : "default";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT);

        return new OpenAPI()
                .info(apiInfo(activeProfile))
                .addSecurityItem(securityRequirement)
                .components(securitySchemes())
                .externalDocs(new ExternalDocumentation()
                        .description("Rabbit API GitHub")
                        .url("https://github.com/lsh2613/RabbitChat")
                );
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    @Bean
    public OperationCustomizer customizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiExceptionExplainParser.parse(operation, handlerMethod);
            return operation;
        };
    }

    private Info apiInfo(String activeProfile) {
        return new Info()
                .title("Rabbit API (" + activeProfile + ")")
                .description("채팅 플랫폼 Rabbit API 명세서")
                .version("v1.0.0");
    }

    private Components securitySchemes() {
        final var securitySchemeAccessToken = new SecurityScheme()
                .name(JWT)
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new Components()
                .addSecuritySchemes(JWT, securitySchemeAccessToken);
    }
}
