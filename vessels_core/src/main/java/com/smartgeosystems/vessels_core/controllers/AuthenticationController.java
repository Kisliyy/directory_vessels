package com.smartgeosystems.vessels_core.controllers;

import com.smartgeosystems.vessels_core.dto.auth.AuthenticationRequest;
import com.smartgeosystems.vessels_core.dto.auth.AuthenticationResponse;
import com.smartgeosystems.vessels_core.dto.auth.RegisterDataRequest;
import com.smartgeosystems.vessels_core.dto.auth.RegisterResponse;
import com.smartgeosystems.vessels_core.dto.exceptions.ExceptionMessageResponse;
import com.smartgeosystems.vessels_core.dto.exceptions.ExceptionValidationResponse;
import com.smartgeosystems.vessels_core.services.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "authentication and registration users")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Add new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User added",
                    content = @Content(schema = @Schema(implementation = RegisterResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ExceptionMessageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Value validation error",
                    content = @Content(schema = @Schema(implementation = ExceptionValidationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403", description = "The user is not authenticated or not authorized"
            )
    })
    @PostMapping(value = "/api/auth/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterDataRequest registerDataRequest) {
        authenticationService.register(registerDataRequest);
        return ResponseEntity
                .ok(new RegisterResponse("The user has been successfully created"));
    }

    @Operation(summary = "Authentication user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User authenticated",
                    content = @Content(schema = @Schema(implementation = AuthenticationRequest.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Value validation error",
                    content = @Content(schema = @Schema(implementation = ExceptionValidationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ExceptionMessageResponse.class))
            ),
    })
    @PostMapping(value = "/api/auth/authentication",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirements
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return ResponseEntity
                .ok(authenticationService.authentication(authenticationRequest));
    }
}
