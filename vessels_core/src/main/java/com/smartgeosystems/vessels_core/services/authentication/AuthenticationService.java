package com.smartgeosystems.vessels_core.services.authentication;

import com.smartgeosystems.vessels_core.dto.auth.AuthenticationRequest;
import com.smartgeosystems.vessels_core.dto.auth.AuthenticationResponse;
import com.smartgeosystems.vessels_core.dto.auth.RegisterDataRequest;

public interface AuthenticationService {
    void register(RegisterDataRequest registerDataRequest);

    AuthenticationResponse authentication(AuthenticationRequest authenticationRequest);
}
