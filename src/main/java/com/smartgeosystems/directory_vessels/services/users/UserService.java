package com.smartgeosystems.directory_vessels.services.users;

import com.smartgeosystems.directory_vessels.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean existsByUsername(String username);

    User findByUsername(String username);

    User save(User createUser);
}
