package com.smartgeosystems.directory_vessels.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartgeosystems.directory_vessels.models.Role;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterDataRequest {

    @JsonProperty(value = "firstName")
    @NotNull
    @NotEmpty
    private String firstName;

    @JsonProperty(value = "lastName")
    @NotNull
    @NotEmpty
    private String lastName;

    @JsonProperty(value = "username")
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "The password does not meet the conditions")
    @JsonProperty(value = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "role")
    @NotNull
    private Role role;

}
