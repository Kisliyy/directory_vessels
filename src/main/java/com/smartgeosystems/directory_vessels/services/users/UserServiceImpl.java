package com.smartgeosystems.directory_vessels.services.users;

import com.smartgeosystems.directory_vessels.exceptions.UserException;
import com.smartgeosystems.directory_vessels.models.User;
import com.smartgeosystems.directory_vessels.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "vessel_users")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var findUser = userRepository.findByUsername(username);
        if (findUser.isPresent()) {
            return findUser.get();
        }
        throw new UsernameNotFoundException("User not found by username: " + username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository
                .existsByUsername(username);
    }

    @Override
    @Cacheable(value = "vessel_users")
    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException("User not found by username: " + username));
    }

    @Override
    @CachePut(value = "vessel_users", key = "#createUser.username")
    public User save(User createUser) {
        return userRepository.save(createUser);
    }
}
