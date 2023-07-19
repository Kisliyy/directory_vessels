package com.smartgeosystems.directory_vessels.services.authentication;

import com.smartgeosystems.directory_vessels.config.jwt.JwtProvider;
import com.smartgeosystems.directory_vessels.dto.auth.AuthenticationRequest;
import com.smartgeosystems.directory_vessels.dto.auth.AuthenticationResponse;
import com.smartgeosystems.directory_vessels.dto.auth.RegisterDataRequest;
import com.smartgeosystems.directory_vessels.exceptions.UserException;
import com.smartgeosystems.directory_vessels.mappers.users.UserMapper;
import com.smartgeosystems.directory_vessels.models.User;
import com.smartgeosystems.directory_vessels.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    @Override
    public void register(RegisterDataRequest registerDataRequest) {
        var byUsername = userService.existsByUsername(registerDataRequest.getUsername());
        if (byUsername) {
            throw new UserException("User with this username already exists!");
        }
        User buildUser = userMapper.registerDataToUser(registerDataRequest);
        userService.save(buildUser);
    }

    @Override
    public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(authenticationToken);
        final var username = authenticationRequest.getUsername();
        var findUser = userService.findByUsername(username);
        String token = jwtProvider.generateToken(findUser);
        return new AuthenticationResponse(token);
    }
}
