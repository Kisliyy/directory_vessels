package com.smartgeosystems.directory_vessels.mappers;

import com.smartgeosystems.directory_vessels.dto.VesselKafkaDto;
import com.smartgeosystems.directory_vessels.models.Vessel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class VesselMapperTest {

    private final VesselMapper vesselMapper = Mappers.getMapper(VesselMapper.class);

    @Test
    void vesselKafkaToVesselSuccessfulMappingTest() {
        final var mmsi = 1234L;
        final var vesselName = "vesselName";
        final var callSign = "callSign";
        final var imo = 1234567L;
        final var shipTypeId = 123L;
        final var destination = "destination";
        final var eta = Instant.now();
        final var dimensionToBowA = 1.2;
        final var dimensionToSternB = 1.4;
        final var dimensionToPortC = 1.6;
        final var dimensionToStarboardD = 1.8;
        final var draught = 2.4;
        final var packageTime = Instant.now();
        var vesselKafkaDto = VesselKafkaDto.builder()
                .mmsi(mmsi)
                .vesselName(vesselName)
                .callSign(callSign)
                .imo(imo)
                .shipTypeId(shipTypeId)
                .destination(destination)
                .eta(eta)
                .dimensionToBowA(dimensionToBowA)
                .dimensionToSternB(dimensionToSternB)
                .dimensionToPortC(dimensionToPortC)
                .dimensionToStarboardD(dimensionToStarboardD)
                .draught(draught)
                .packageTime(packageTime)
                .build();

        Vessel vessel = vesselMapper.vesselKafkaToVessel(vesselKafkaDto);
        assertAll("Group vessel mapper assertions",
                () -> assertEquals(mmsi, vessel.getMmsi(), "Mmsi must be mapping"),
                () -> assertEquals(vesselName, vessel.getVesselName(), "Vessel name must be mapping"),
                () -> assertEquals(callSign, vessel.getCallSign(), "Call sign must be mapping"),
                () -> assertEquals(imo, vessel.getImo(), "Imo must be mapping"),
                () -> assertEquals(shipTypeId, vessel.getShipTypeId(), "Ship type id must be mapping"),
                () -> assertEquals(destination, vessel.getDestination(), "Destination must be mapping"),
                () -> assertEquals(eta.getNano(), vessel.getEta().getNanos(), "Eta must be mapping"),
                () -> assertEquals(dimensionToBowA, vessel.getDimensionToBowA(), "Dimension To Bow A must be mapping"),
                () -> assertEquals(dimensionToSternB, vessel.getDimensionToSternB(), "Dimension To Stern B must be mapping"),
                () -> assertEquals(dimensionToPortC, vessel.getDimensionToPortC(), "Dimension To Port C must be mapping"),
                () -> assertEquals(dimensionToStarboardD, vessel.getDimensionToStarboardD(), "Dimension To Starboard D must be mapping"),
                () -> assertEquals(draught, vessel.getDraught(), "Draught must be mapping"),
                () -> assertEquals(packageTime.getNano(), vessel.getPackageTime().getNanos(), "Package Time must be mapping"),
                () -> assertFalse(vessel.isDeleted(), "Deleted must be default is false")
        );

    }
}