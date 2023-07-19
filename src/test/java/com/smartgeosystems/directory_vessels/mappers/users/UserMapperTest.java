package com.smartgeosystems.directory_vessels.mappers.users;

import com.smartgeosystems.directory_vessels.dto.auth.RegisterDataRequest;
import com.smartgeosystems.directory_vessels.models.Role;
import com.smartgeosystems.directory_vessels.models.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {UserMapperImpl.class})
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void registerDataToUserMapperTest() {
        var firstName = "firstName";
        var lastName = "lastName";
        var username = "username";
        var password = "password";
        var role = Role.USER;

        var encodePassword = "encodePassword";

        Mockito.when(passwordEncoder.encode(password))
                .thenReturn(encodePassword);

        var request = new RegisterDataRequest(firstName, lastName, username, password, role);
        User user = userMapper.registerDataToUser(request);
        assertAll("Group assertions user mapper",
                () -> assertNotNull(user, "The user cannot be null"),
                () -> assertEquals(firstName, user.getFirstName(), "First name must be moved"),
                () -> assertEquals(lastName, user.getLastName(), "Last name must be moved"),
                () -> assertEquals(username, user.getUsername(), "Username must be moved"),
                () -> assertEquals(encodePassword, user.getPassword(), "Password must be moved and encoded"),
                () -> assertEquals(role, user.getRole(), "Role must be moved"),
                () -> assertNull(user.getId(), "Id must be null"),
                () -> assertNull(user.getCreationTime(), "Creation time must be null")
        );
    }
}