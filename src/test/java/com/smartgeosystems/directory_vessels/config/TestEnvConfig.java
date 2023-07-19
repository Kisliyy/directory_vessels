package com.smartgeosystems.directory_vessels.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.smartgeosystems.directory_vessels",lazyInit = true)
public class TestEnvConfig {
}
