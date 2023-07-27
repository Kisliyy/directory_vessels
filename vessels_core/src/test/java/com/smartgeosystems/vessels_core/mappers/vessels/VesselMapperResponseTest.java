package com.smartgeosystems.vessels_core.mappers.vessels;

import com.smartgeosystems.vessels_core.dto.vessels.VesselResponseDto;
import com.smartgeosystems.vessels_core.models.Vessel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


class VesselMapperResponseTest {

    private final VesselMapperResponse vesselMapperResponse = Mappers.getMapper(VesselMapperResponse.class);

    @Test
    void vesselToVesselResponseDtoSuccessfulMappingTest() {
        long time = new Date().getTime() - 10000;
        final Long mmsi = 123456789881L;
        final Long imo = 111111111L;
        final Long mid = null;
        final String vesselName = "vesselName";
        final String callSign = "callSign";
        final Long shipTypeId = 12356L;
        final String destination = "destination";
        final Timestamp eta = new Timestamp(time);
        final Double dimensionToBowA = 2d;
        final Double dimensionToSternB = 1.3;
        final Double genLength = dimensionToBowA + dimensionToSternB;
        final Double dimensionToPortC = 3d;
        final Double dimensionToStarboardD = 4d;
        final Double genWidth = dimensionToPortC + dimensionToStarboardD;
        final Double draught = 3d;
        final Timestamp packageTime = new Timestamp(time);
        final boolean deleted = false;

        var vessel = Vessel.builder()
                .imo(imo)
                .mmsi(mmsi)
                .mid(mid)
                .vesselName(vesselName)
                .callSign(callSign)
                .shipTypeId(shipTypeId)
                .destination(destination)
                .eta(eta)
                .dimensionToBowA(dimensionToBowA)
                .dimensionToSternB(dimensionToSternB)
                .genLength(genLength)
                .dimensionToPortC(dimensionToPortC)
                .dimensionToStarboardD(dimensionToStarboardD)
                .genWidth(genWidth)
                .draught(draught)
                .packageTime(packageTime)
                .deleted(deleted)
                .build();

        VesselResponseDto vesselResponseDto = vesselMapperResponse.vesselToVesselResponseDto(vessel);

        assertAll("Group vessel mapper assertions",
                () -> assertEquals(mmsi, vesselResponseDto.getMmsi(), "Mmsi must be moved!"),
                () -> assertEquals(vesselName, vesselResponseDto.getVesselName(), "Vessel name must be moved!"),
                () -> assertEquals(imo, vessel.getImo(), "Imo must be moved!"),
                () -> assertEquals(callSign, vesselResponseDto.getCallSign(), "Call sign must be moved!"),
                () -> assertEquals(shipTypeId, vesselResponseDto.getShipTypeId(), "Ship type id must be moved!"),
                () -> assertEquals(destination, vesselResponseDto.getDestination(), "Destination must be moved!"),
                () -> assertEquals(eta, vesselResponseDto.getEta(), "Eta must be moved!"),
                () -> assertEquals(dimensionToBowA, vesselResponseDto.getDimensionToBowA(), "Dimension To Bow A must be moved!"),
                () -> assertEquals(dimensionToSternB, vesselResponseDto.getDimensionToSternB(), "Dimension To Stern B must be moved!"),
                () -> assertEquals(dimensionToPortC, vesselResponseDto.getDimensionToPortC(), "Dimension To Port C must be moved!"),
                () -> assertEquals(dimensionToStarboardD, vesselResponseDto.getDimensionToStarboardD(), "Dimension To Starboard D must be moved!"),
                () -> assertEquals(draught, vesselResponseDto.getDraught(), "Draught must be moved!"),
                () -> assertEquals(packageTime, vesselResponseDto.getPackageTime(), "Package Time must be moved!"),
                () -> assertEquals(genLength, vesselResponseDto.getGenLength(), "Gen length must be moved!!"),
                () -> assertEquals(genWidth, vesselResponseDto.getGenWidth(), "Gen width must be moved!"),
                () -> assertNull(vesselResponseDto.getMid(), "Mid must to be moved!")
        );
    }

}