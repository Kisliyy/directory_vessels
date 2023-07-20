package com.smartgeosystems.directory_vessels.mappers.vessels;

import com.smartgeosystems.directory_vessels.dto.vessels.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.vessels.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.utils.MIDUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
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
        return (dimensionToBowA == null ? 0 : dimensionToBowA) +
                (dimensionToSternB == null ? 0 : dimensionToSternB);
    }

    @Named(value = "dimensionToGenWidth")
    default Double mapGenWidth(VesselRequestDto vesselRequestDto) {
        var dimensionToPortC = vesselRequestDto.getDimensionToPortC();
        var dimensionToStarboardD = vesselRequestDto.getDimensionToStarboardD();
        return (dimensionToPortC == null ? 0 : dimensionToPortC) +
                (dimensionToStarboardD == null ? 0 : dimensionToStarboardD);
    }

    @Named(value = "dimensionToGenLength")
    default Double mapGenLength(VesselUpdateDto vesselUpdateDto) {
        var dimensionToBowA = vesselUpdateDto.getDimensionToBowA();
        var dimensionToSternB = vesselUpdateDto.getDimensionToSternB();
        return (dimensionToBowA == null ? 0 : dimensionToBowA) +
                (dimensionToSternB == null ? 0 : dimensionToSternB);
    }

    @Named(value = "dimensionToGenWidth")
    default Double mapGenWidth(VesselUpdateDto vesselUpdateDto) {
        var dimensionToPortC = vesselUpdateDto.getDimensionToPortC();
        var dimensionToStarboardD = vesselUpdateDto.getDimensionToStarboardD();
        return (dimensionToPortC == null ? 0 : dimensionToPortC) +
                (dimensionToStarboardD == null ? 0 : dimensionToStarboardD);
    }
}
