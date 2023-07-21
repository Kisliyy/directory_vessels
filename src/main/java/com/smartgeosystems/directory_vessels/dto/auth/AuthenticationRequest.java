package com.smartgeosystems.directory_vessels.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
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
