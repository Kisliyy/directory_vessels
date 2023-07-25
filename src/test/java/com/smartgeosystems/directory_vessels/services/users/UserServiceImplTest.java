package com.smartgeosystems.directory_vessels.services.users;

import com.smartgeosystems.directory_vessels.exceptions.UserException;
import com.smartgeosystems.directory_vessels.models.User;
import com.smartgeosystems.directory_vessels.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void loadByUsernameWhenUserNotFound() {
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(anyString()));

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void loadByUsernameWhenUserExist() {
        final var username = "username";
        User findUser = User.builder()
                .username(username)
                .build();

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(findUser));

        UserDetails userDetails = userService
                .loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void existByUsernameWhenUserNotFound() {
        when(userRepository.existsByUsername(anyString()))
                .thenReturn(false);

        boolean existsByUsername = userService.existsByUsername(anyString());
        assertFalse(existsByUsername);

        verify(userRepository, times(1)).existsByUsername(anyString());
    }

    @Test
    void existByUsernameWhenUserExist() {
        when(userRepository.existsByUsername(anyString()))
                .thenReturn(true);

        boolean existsByUsername = userService.existsByUsername(anyString());
        assertTrue(existsByUsername);

        verify(userRepository, times(1)).existsByUsername(anyString());
    }


    @Test
    void findByUsernameWhenUserNotFound() {
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.findByUsername(anyString()));

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void findByUsernameWhenUserExist() {
        final var username = "username";
        User findUser = User.builder()
                .username(username)
                .build();

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(findUser));

        var user = userService
                .findByUsername(username);

        assertNotNull(user);
        assertEquals(username, user.getUsername());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void saveUserTest() {
        final var username = "username";
        User saveUser = User.builder()
                .username(username)
                .build();

        when(userRepository.save(saveUser))
                .thenReturn(saveUser);

        var user = userService
                .save(saveUser);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }


}