package com.smartgeosystems.vessels_core.mappers.vessels;

import com.smartgeosystems.utils.MIDUtils;
import com.smartgeosystems.utils.VesselUtils;
import com.smartgeosystems.vessels_core.dto.vessels.VesselRequestDto;
import com.smartgeosystems.vessels_core.dto.vessels.VesselUpdateDto;
import com.smartgeosystems.vessels_core.models.Vessel;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VesselMapper {

    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", defaultValue = "false", ignore = true)
    @Mapping(target = "mid", source = "mmsi", qualifiedByName = "mmsiToMid")
    @Mapping(target = "genLength", source = ".", qualifiedByName = "dimensionToGenLength")
    @Mapping(target = "genWidth", source = ".", qualifiedByName = "dimensionToGenWidth")
    Vessel vesselRequestDtoToVessel(VesselRequestDto vesselRequestDto);


    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", defaultValue = "false", ignore = true)
    @Mapping(target = "mid", source = "mmsi", qualifiedByName = "mmsiToMid")
    @Mapping(target = "genLength", source = ".", qualifiedByName = "dimensionToGenLength")
    @Mapping(target = "genWidth", source = ".", qualifiedByName = "dimensionToGenWidth")
    Vessel vesselUpdateDtoToVessel(VesselUpdateDto vesselUpdateDto);

    @Mapping(target = "imo", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "mid", source = "mmsi", qualifiedByName = "mmsiToMid")
    @Mapping(target = "genLength", source = ".", qualifiedByName = "dimensionToGenLength")
    @Mapping(target = "genWidth", source = ".", qualifiedByName = "dimensionToGenWidth")
    void updateVessel(@MappingTarget Vessel vessel, VesselUpdateDto vesselUpdateDto);

    @Named(value = "mmsiToMid")
    default Long mapMid(Long mmsi) {
        return MIDUtils.mmsiToMid(mmsi);
    }

    @Named(value = "dimensionToGenLength")
    default Double mapGenLength(VesselRequestDto vesselRequestDto) {
        var dimensionToBowA = vesselRequestDto.getDimensionToBowA();
        var dimensionToSternB = vesselRequestDto.getDimensionToSternB();
        return VesselUtils.genLength(dimensionToBowA, dimensionToSternB);
    }

    @Named(value = "dimensionToGenWidth")
    default Double mapGenWidth(VesselRequestDto vesselRequestDto) {
        var dimensionToPortC = vesselRequestDto.getDimensionToPortC();
        var dimensionToStarboardD = vesselRequestDto.getDimensionToStarboardD();
        return VesselUtils.genWidth(dimensionToPortC, dimensionToStarboardD);
    }

    @Named(value = "dimensionToGenLength")
    default Double mapGenLength(VesselUpdateDto vesselUpdateDto) {
        var dimensionToBowA = vesselUpdateDto.getDimensionToBowA();
        var dimensionToSternB = vesselUpdateDto.getDimensionToSternB();
        return VesselUtils.genLength(dimensionToBowA, dimensionToSternB);
    }

    @Named(value = "dimensionToGenWidth")
    default Double mapGenWidth(VesselUpdateDto vesselUpdateDto) {
        var dimensionToPortC = vesselUpdateDto.getDimensionToPortC();
        var dimensionToStarboardD = vesselUpdateDto.getDimensionToStarboardD();
        return VesselUtils.genWidth(dimensionToPortC, dimensionToStarboardD);
    }
}
