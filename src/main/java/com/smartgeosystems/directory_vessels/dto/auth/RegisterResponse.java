package com.smartgeosystems.directory_vessels.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    @JsonProperty(value = "message")
    private String message;
}
