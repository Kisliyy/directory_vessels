package com.smartgeosystems.vessels_core.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationRequest {

    @NotNull
    @NotEmpty
    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "password")
    @NotNull
    @NotEmpty
    private String password;
}
