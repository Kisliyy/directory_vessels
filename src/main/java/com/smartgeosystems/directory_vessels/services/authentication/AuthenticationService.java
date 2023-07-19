package com.smartgeosystems.directory_vessels.services.authentication;

import com.smartgeosystems.directory_vessels.dto.auth.AuthenticationRequest;
import com.smartgeosystems.directory_vessels.dto.auth.AuthenticationResponse;
import com.smartgeosystems.directory_vessels.dto.auth.RegisterDataRequest;

public interface AuthenticationService {
    void register(RegisterDataRequest registerDataRequest);

    AuthenticationResponse authentication(AuthenticationRequest authenticationRequest);
}
