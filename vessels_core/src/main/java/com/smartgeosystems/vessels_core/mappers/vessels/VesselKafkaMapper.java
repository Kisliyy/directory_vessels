package com.smartgeosystems.vessels_core.mappers.vessels;

import com.smartgeosystems.utils.MIDUtils;
import com.smartgeosystems.utils.VesselUtils;
import com.smartgeosystems.vessels_core.models.Vessel;
import org.mapstruct.*;
import org.vmts.vessel.VesselInfo;

import java.sql.Timestamp;
import java.time.Instant;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VesselKafkaMapper {

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
        return VesselUtils.genLength(dimensionToBowA, dimensionToSternB);
    }

    @Named(value = "dimensionToGenWidth")
    default Double mapGenWidth(VesselInfo vesselInfo) {
        var dimensionToPortC = vesselInfo.getDimensionToPortC();
        var dimensionToStarboardD = vesselInfo.getDimensionToStarboardD();
        return VesselUtils.genWidth(dimensionToPortC, dimensionToStarboardD);
    }
}
