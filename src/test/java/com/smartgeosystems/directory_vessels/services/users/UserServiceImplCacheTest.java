package com.smartgeosystems.directory_vessels.services.users;

import com.smartgeosystems.directory_vessels.config.CacheConfig;
import com.smartgeosystems.directory_vessels.models.Role;
import com.smartgeosystems.directory_vessels.models.User;
import com.smartgeosystems.directory_vessels.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        UserServiceImpl.class,
        CacheConfig.class,
})
@ActiveProfiles(value = "test")
class UserServiceImplCacheTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;
    private final String username = "admin";
    private User buildUser;

    @BeforeEach
    void init() {
        buildUser = User.builder()
                .id(2L)
                .username(username)
                .role(Role.ADMIN)
                .firstName("firstname")
                .lastName("lastname")
                .build();
    }

    @AfterEach
    void after() {
        Cache cacheUsers = cacheManager.getCache("vessel_users");
        Assertions.assertNotNull(cacheUsers);
        cacheUsers.clear();
    }

    @Test
    void loadByUsernameCacheTest() {
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(buildUser));

        userService.loadUserByUsername(username);
        userService.loadUserByUsername(username);
        userService.loadUserByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);
    }


    @Test
    void findByUsernameCacheTest() {
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(buildUser));

        userService.findByUsername(username);
        userService.findByUsername(username);
        userService.findByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void saveNewUserCacheTest() {
        when(userRepository.save(buildUser))
                .thenReturn(buildUser);

        userService.save(buildUser);

        Cache cache = cacheManager.getCache("vessel_users");
        Assertions.assertNotNull(cache);
        User cacheUser = cache.get(username, User.class);
        Assertions.assertNotNull(cacheUser);
        Assertions.assertEquals(buildUser, cacheUser);
    }


    @Test
    void saveExistUserInCacheTest() {
        User updateUser = User
                .builder()
                .id(123L)
                .role(Role.USER)
                .username(username)
                .build();

        when(userRepository.save(buildUser))
                .thenReturn(buildUser);

        when(userRepository.save(updateUser))
                .thenReturn(updateUser);

        userService.save(buildUser);
        userService.save(updateUser);

        Cache cache = cacheManager.getCache("vessel_users");

        Assertions.assertNotNull(cache);
        User updateCacheUser = cache.get(username, User.class);
        Assertions.assertNotNull(updateCacheUser);
        Assertions.assertEquals(updateUser, updateCacheUser);
    }
}