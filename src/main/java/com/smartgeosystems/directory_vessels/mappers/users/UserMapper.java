package com.smartgeosystems.directory_vessels.mappers.users;

import com.smartgeosystems.directory_vessels.dto.auth.RegisterDataRequest;
import com.smartgeosystems.directory_vessels.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {


    @Autowired
    protected PasswordEncoder passwordEncoder;


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(registerDataRequest.getPassword()))")
    public abstract User registerDataToUser(RegisterDataRequest registerDataRequest);
}
