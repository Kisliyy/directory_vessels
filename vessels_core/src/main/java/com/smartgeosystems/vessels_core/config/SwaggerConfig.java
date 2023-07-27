package com.smartgeosystems.vessels_core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Directory Vessels API", version = "1.0", description = "Directory Vessels"),
        security = {@SecurityRequirement(name = "Bearer authentication")}
)
@SecurityScheme(name = "Bearer authentication",
        type = SecuritySchemeType.HTTP,
        paramName = "Bearer authentication",
        bearerFormat = "JWT",
        scheme = "Bearer",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
