package com.smartgeosystems.vessels_core.config;

import com.smartgeosystems.vessels_core.config.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = JwtProperties.class)
public class ApplicationProperties {
}
