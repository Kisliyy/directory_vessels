package com.smartgeosystems.vessels_core.services.users;

import com.smartgeosystems.vessels_core.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean existsByUsername(String username);

    User findByUsername(String username);

    User save(User createUser);
}
