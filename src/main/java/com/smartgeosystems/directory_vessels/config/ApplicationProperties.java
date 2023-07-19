package com.smartgeosystems.directory_vessels.config;

import com.smartgeosystems.directory_vessels.config.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = JwtProperties.class)
public class ApplicationProperties {
}
