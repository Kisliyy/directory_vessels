package com.smartgeosystems.directory_vessels.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartgeosystems.directory_vessels.dto.auth.AuthenticationRequest;
import com.smartgeosystems.directory_vessels.dto.auth.AuthenticationResponse;
import com.smartgeosystems.directory_vessels.dto.auth.RegisterDataRequest;
import com.smartgeosystems.directory_vessels.models.Role;
import com.smartgeosystems.directory_vessels.services.authentication.AuthenticationService;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AuthenticationController.class)
@AutoConfigureCache
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ContextConfiguration
@WebAppConfiguration
class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }


    @Test
    void authenticationOfTheCreatedUser() throws Exception {
        var token = "token";
        var response = new AuthenticationResponse(token);

        var username = "username";
        var password = "password";
        var request = new AuthenticationRequest(username, password);

        when(authenticationService.authentication(request))
                .thenReturn(response);

        mockMvc
                .perform(
                        post("/api/auth/authentication")
                                .content(objectToJson(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token", Matchers.is(token)));

        verify(authenticationService, times(1)).authentication(request);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void registrationOfANewUserByTheAdministrator() throws Exception {
        final var firstName = "firstName";
        final var lastName = "lastName";
        final var username = "username";
        final var password = "Masd1*/qwe2";
        final var role = Role.USER;

        var registerRequest = RegisterDataRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password)
                .role(role)
                .build();


        doNothing()
                .when(authenticationService)
                .register(registerRequest);

        mockMvc
                .perform(
                        post("/api/auth/register")
                                .content(objectToJson(registerRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Matchers.is("The user has been successfully created")));

        verify(authenticationService, times(1)).register(registerRequest);
    }

    private String objectToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}