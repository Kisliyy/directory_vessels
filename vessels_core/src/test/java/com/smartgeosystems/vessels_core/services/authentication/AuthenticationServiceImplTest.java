package com.smartgeosystems.vessels_core.services.authentication;

import com.smartgeosystems.vessels_core.config.jwt.JwtProvider;
import com.smartgeosystems.vessels_core.dto.auth.RegisterDataRequest;
import com.smartgeosystems.vessels_core.exceptions.UserException;
import com.smartgeosystems.vessels_core.mappers.users.UserMapper;
import com.smartgeosystems.vessels_core.models.Role;
import com.smartgeosystems.vessels_core.models.User;
import com.smartgeosystems.vessels_core.services.users.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = AuthenticationServiceImpl.class)
class AuthenticationServiceImplTest {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private AuthenticationManager authenticationManager;

    final String firstName = "firstName";
    final String lastName = "lastName";
    final String username = "username";
    final String password = "password";
    final Role role = Role.USER;
    private RegisterDataRequest request;

    @BeforeEach
    void init() {
        request = new RegisterDataRequest(firstName, lastName, username, password, role);
    }

    @Test
    void registerSuccessfulTest() {
        final var encodePassword = "encodePassword";

        var build = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(encodePassword)
                .role(role)
                .build();

        var id = 123L;

        var persist = User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(encodePassword)
                .role(role)
                .build();


        when(userService.existsByUsername(username))
                .thenReturn(false);

        when(userMapper.registerDataToUser(request))
                .thenReturn(build);

        when(userService.save(build))
                .thenReturn(persist);

        authenticationService.register(request);

        verify(userService, times(1)).existsByUsername(username);
        verify(userMapper, times(1)).registerDataToUser(request);
        verify(userService, times(1)).save(build);
    }

    @Test
    void registerUserIfUserExistReturnException() {
        when(userService.existsByUsername(username))
                .thenReturn(true);

        Assertions.assertThrows(UserException.class, () -> authenticationService.register(request));

        verify(userService, times(1)).existsByUsername(username);
        verify(userMapper, times(0)).registerDataToUser(any());
        verify(userService, times(0)).save(any());
    }

}