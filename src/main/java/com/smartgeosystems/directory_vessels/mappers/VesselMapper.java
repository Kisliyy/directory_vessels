package com.smartgeosystems.directory_vessels.mappers;

import com.smartgeosystems.directory_vessels.dto.VesselKafkaDto;
import com.smartgeosystems.directory_vessels.models.Vessel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.Instant;

@Mapper
public interface VesselMapper {

    @Mapping(target = "eta")
    @Mapping(target = "packageTime")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", defaultValue = "false", ignore = true)
    Vessel vesselKafkaToVessel(VesselKafkaDto vesselKafkaDto);

    default Timestamp map(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }
}
