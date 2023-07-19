package com.smartgeosystems.directory_vessels.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secretKey;
    @Value("#{${jwt.expiration_date} ?: 86400000}")
    private Long expirationDate;
}
