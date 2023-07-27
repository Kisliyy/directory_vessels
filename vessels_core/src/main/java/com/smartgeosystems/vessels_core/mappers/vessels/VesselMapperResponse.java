package com.smartgeosystems.vessels_core.mappers.vessels;

import com.smartgeosystems.vessels_core.dto.vessels.VesselResponseDto;
import com.smartgeosystems.vessels_core.models.Vessel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VesselMapperResponse {

    VesselResponseDto vesselToVesselResponseDto(Vessel vessel);
}
