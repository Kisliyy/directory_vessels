package com.smartgeosystems.directory_vessels.mappers;

import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.utils.MIDUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.vmts.vessel.VesselInfo;

import java.sql.Timestamp;
import java.time.Instant;

@Mapper
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
    void updateVessel(@MappingTarget Vessel vessel, VesselInfo vesselInfo);

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
}
