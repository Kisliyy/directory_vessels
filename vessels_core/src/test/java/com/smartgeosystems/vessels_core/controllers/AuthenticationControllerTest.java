package com.smartgeosystems.vessels_core.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartgeosystems.vessels_core.config.kafka.KafkaVesselListener;
import com.smartgeosystems.vessels_core.dto.auth.RegisterDataRequest;
import com.smartgeosystems.vessels_core.models.Role;
import com.smartgeosystems.vessels_core.services.authentication.AuthenticationService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.Charset;

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
class AuthenticationControllerTest {

    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;


    @MockBean
    private KafkaVesselListener kafkaVesselListener;
    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void registrationOfANewUserByTheAdministrator() throws Exception {
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


        doNothing()
                .when(authenticationService)
                .register(registerRequest);

        mockMvc
                .perform(
                        post("/api/auth/register")
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