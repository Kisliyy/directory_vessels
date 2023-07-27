package com.smartgeosystems.vessels_batch.mapper;

import com.smartgeosystems.utils.MIDUtils;
import com.smartgeosystems.utils.VesselUtils;
import com.smartgeosystems.vessels_batch.dto.VesselCsvDto;
import com.smartgeosystems.vessels_batch.dto.VesselDbDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BatchMapper {


    @Mapping(target = "creationTime", source = ".", qualifiedByName = "creationTime")
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", defaultValue = "false", ignore = true)
    @Mapping(target = "mid", source = "mmsi", qualifiedByName = "mmsiToMid")
    @Mapping(target = "genLength", source = ".", qualifiedByName = "dimensionToGenLength")
    @Mapping(target = "genWidth", source = ".", qualifiedByName = "dimensionToGenWidth")
    VesselDbDto mapCsv(VesselCsvDto vesselCsvDto);

    @Named(value = "creationTime")
    default Timestamp creationTime(VesselCsvDto vesselCsvDto) {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    @Named(value = "mmsiToMid")
    default Long mapMid(Long mmsi) {
        return MIDUtils.mmsiToMid(mmsi);
    }

    @Named(value = "dimensionToGenLength")
    default Double mapGenLength(VesselCsvDto vesselCsvDto) {
        var dimensionToBowA = vesselCsvDto.getDimensionToBowA();
        var dimensionToSternB = vesselCsvDto.getDimensionToSternB();
        return VesselUtils.genLength(dimensionToBowA, dimensionToSternB);
    }

    @Named(value = "dimensionToGenWidth")
    default Double mapGenWidth(VesselCsvDto vesselCsvDto) {
        var dimensionToPortC = vesselCsvDto.getDimensionToPortC();
        var dimensionToStarboardD = vesselCsvDto.getDimensionToStarboardD();
        return VesselUtils.genWidth(dimensionToPortC, dimensionToStarboardD);
    }
}
