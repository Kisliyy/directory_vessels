package com.smartgeosystems.directory_vessels.services.authentication;

import com.smartgeosystems.directory_vessels.config.jwt.JwtProvider;
import com.smartgeosystems.directory_vessels.dto.auth.AuthenticationRequest;
import com.smartgeosystems.directory_vessels.dto.auth.AuthenticationResponse;
import com.smartgeosystems.directory_vessels.dto.auth.RegisterDataRequest;
import com.smartgeosystems.directory_vessels.exceptions.UserException;
import com.smartgeosystems.directory_vessels.models.User;
import com.smartgeosystems.directory_vessels.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterDataRequest registerDataRequest) {
        var byUsername = userRepository.existsByUsername(registerDataRequest.getUsername());
        if (byUsername) {
            throw new UserException("User with this username already exists!");
        }
        var buildUser = User.builder()
                .username(registerDataRequest.getUsername())
                .firstName(registerDataRequest.getFirstName())
                .lastName(registerDataRequest.getLastName())
                .password(passwordEncoder.encode(registerDataRequest.getPassword()))
                .build();
        userRepository.save(buildUser);
    }

    @Override
    public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(authenticationToken);
        final var username = authenticationRequest.getUsername();
        var findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("User not found by username: " + username));
        String token = jwtProvider.generateToken(findUser);
        return new AuthenticationResponse(token);
    }
}
