package com.smartgeosystems.directory_vessels.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterDataRequest {

    @JsonProperty(value = "firstName")
    @NotNull
    private String firstName;
    @JsonProperty(value = "lastName")
    @NotNull
    private String lastName;
    @JsonProperty(value = "username")
    @NotNull
    @Min(value = 5)
    private String username;

    @NotNull
    @Min(value = 8)
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$)",
            message = "The password does not meet the conditions")
    private String password;

}
