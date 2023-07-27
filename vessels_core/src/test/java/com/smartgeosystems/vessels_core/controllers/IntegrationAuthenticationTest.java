package com.smartgeosystems.vessels_core.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartgeosystems.vessels_core.config.jwt.JwtProvider;
import com.smartgeosystems.vessels_core.config.kafka.KafkaVesselListener;
import com.smartgeosystems.vessels_core.dto.auth.AuthenticationRequest;
import com.smartgeosystems.vessels_core.dto.auth.AuthenticationResponse;
import com.smartgeosystems.vessels_core.dto.auth.RegisterDataRequest;
import com.smartgeosystems.vessels_core.models.Role;
import com.smartgeosystems.vessels_core.models.User;
import com.smartgeosystems.vessels_core.services.authentication.AuthenticationService;
import com.smartgeosystems.vessels_core.services.users.UserService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.Charset;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureCache
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("test")
public class IntegrationAuthenticationTest {


    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private KafkaVesselListener kafkaVesselListener;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
    }


    @Test
    void authenticationOfTheCreatedUser() throws Exception {
        var token = "token";
        var response = new AuthenticationResponse(token);

        var username = "username";
        var password = "password";
        var request = new AuthenticationRequest(username, password);

        when(authenticationService.authentication(eq(request)))
                .thenReturn(response);

        mockMvc
                .perform(
                        post("/api/auth/authentication")
                                .content(objectToJson(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(Charset.defaultCharset())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token", Matchers.is(token)));

        verify(authenticationService, times(1)).authentication(request);
    }


    @Test
    void registrationOfANewUser() throws Exception {
        final var firstName = "firstName";
        final var lastName = "lastName";
        final var username = "username";
        final var password = "Masd1*/qwe2=";
        final var role = Role.USER;

        var registerRequest = RegisterDataRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password)
                .role(role)
                .build();

        final User admin = userService.findByUsername("admin");
        final String token = jwtProvider.generateToken(admin);

        doNothing()
                .when(authenticationService)
                .register(registerRequest);

        mockMvc
                .perform(
                        post("/api/auth/register")
                                .header("Authorization", "Bearer " + token)
                                .content(objectToJson(registerRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(Charset.defaultCharset())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Matchers.is("The user has been successfully created")));

        verify(authenticationService, atMostOnce()).register(registerRequest);
    }

    private String objectToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}

