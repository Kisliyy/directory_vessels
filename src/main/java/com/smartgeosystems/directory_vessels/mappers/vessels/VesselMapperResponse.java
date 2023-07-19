package com.smartgeosystems.directory_vessels.mappers.vessels;

import com.smartgeosystems.directory_vessels.dto.vessels.VesselResponseDto;
import com.smartgeosystems.directory_vessels.models.Vessel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VesselMapperResponse {

    VesselResponseDto vesselToVesselResponseDto(Vessel vessel);
}
