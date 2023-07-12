package com.smartgeosystems.directory_vessels.mappers;

import com.smartgeosystems.directory_vessels.dto.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.utils.MIDUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.vmts.vessel.VesselInfo;

import java.sql.Timestamp;
import java.time.Instant;

@Mapper(componentModel = "spring")
public interface VesselMapper {

    @Mapping(target = "eta")
    @Mapping(target = "packageTime")
    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", defaultValue = "false", ignore = true)
    @Mapping(target = "mid", source = "mmsi", qualifiedByName = "mmsiToMid")
    @Mapping(target = "genLength", source = ".", qualifiedByName = "dimensionToGenLength")
    @Mapping(target = "genWidth", source = ".", qualifiedByName = "dimensionToGenWidth")
    Vessel vesselKafkaToVessel(VesselInfo vesselInfo);

    @Mapping(target = "imo", ignore = true)
    @Mapping(target = "eta")
    @Mapping(target = "packageTime")
    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", defaultValue = "false", ignore = true)
    @Mapping(target = "mid", source = "mmsi", qualifiedByName = "mmsiToMid")
    @Mapping(target = "genLength", source = ".", qualifiedByName = "dimensionToGenLength")
    @Mapping(target = "genWidth", source = ".", qualifiedByName = "dimensionToGenWidth")
    void updateVesselKafkaToVessel(@MappingTarget Vessel vessel, VesselInfo vesselInfo);


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


    default Timestamp map(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }

    @Named(value = "mmsiToMid")
    default Long mapMid(Long mmsi) {
        return MIDUtils.mmsiToMid(mmsi);
    }

    @Named(value = "dimensionToGenLength")
    default Double mapGenLength(VesselInfo vesselInfo) {
        var dimensionToBowA = vesselInfo.getDimensionToBowA();
        var dimensionToSternB = vesselInfo.getDimensionToSternB();
        return (dimensionToBowA == null ? 0 : dimensionToBowA) +
                (dimensionToSternB == null ? 0 : dimensionToSternB);
    }

    @Named(value = "dimensionToGenWidth")
    default Double mapGenWidth(VesselInfo vesselInfo) {
        var dimensionToPortC = vesselInfo.getDimensionToPortC();
        var dimensionToStarboardD = vesselInfo.getDimensionToStarboardD();
        return (dimensionToPortC == null ? 0 : dimensionToPortC) +
                (dimensionToStarboardD == null ? 0 : dimensionToStarboardD);
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
