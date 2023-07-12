package com.smartgeosystems.directory_vessels.mappers;

import com.smartgeosystems.directory_vessels.dto.VesselResponseDto;
import com.smartgeosystems.directory_vessels.models.Vessel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VesselMapperResponse {

    VesselResponseDto vesselToVesselResponseDto(Vessel vessel);
}
