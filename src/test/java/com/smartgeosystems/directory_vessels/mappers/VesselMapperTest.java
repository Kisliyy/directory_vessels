package com.smartgeosystems.directory_vessels.mappers;

import com.smartgeosystems.directory_vessels.dto.VesselKafkaDto;
import com.smartgeosystems.directory_vessels.models.Vessel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vmts.vessel.VesselInfo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class VesselMapperTest {

    private final VesselMapper vesselMapper = Mappers.getMapper(VesselMapper.class);

    final Long mmsi = 111234567L;
    final String vesselName = "vesselName";
    final String callSign = "callSign";
    final Long imo = 1234567L;
    final Long shipTypeId = 123L;
    final String destination = "destination";
    final Instant eta = Instant.now();
    final Double dimensionToBowA = 1.2;
    final Double dimensionToSternB = 1.4;
    final Double genLength = dimensionToBowA + dimensionToSternB;
    final Double dimensionToPortC = 1.6;
    final Double dimensionToStarboardD = 1.8;
    final Double genWidth = dimensionToPortC + dimensionToStarboardD;
    final Double draught = 2.4;
    final Instant packageTime = Instant.now();

    private VesselInfo vesselInfo;

    @BeforeEach
    void init() {
        vesselInfo = VesselInfo.newBuilder()
                .setMmsi(mmsi)
                .setVesselName(vesselName)
                .setCallSign(callSign)
                .setImo(imo)
                .setShipTypeId(shipTypeId)
                .setDestination(destination)
                .setEta(eta)
                .setDimensionToBowA(dimensionToBowA)
                .setDimensionToSternB(dimensionToSternB)
                .setDimensionToPortC(dimensionToPortC)
                .setDimensionToStarboardD(dimensionToStarboardD)
                .setDraught(draught)
                .setPackageTime(packageTime)
                .build();
    }


    @Test
    void vesselKafkaToVesselSuccessfulMappingTest() {
        Vessel vessel = vesselMapper.vesselKafkaToVessel(vesselInfo);
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
                () -> assertFalse(vessel.isDeleted(), "Deleted must be default is false"),
                () -> assertEquals(genLength, vessel.getGenLength(), "Gen length must be mapping"),
                () -> assertEquals(genWidth, vessel.getGenWidth(), "Gen width must be mapping"),
                () -> assertEquals(234L, vessel.getMid(), "Mid must be equal to expected value")
        );
    }


    @Test
    void updateVessel() {
        long oldTime = new Date().getTime() - 10000;
        final Long oldMmsi = 123456789881L;
        final Long oldImo = 111111111L;
        final Long oldMid = null;
        final String oldVesselName = "oldVesselName";
        final String oldCallSign = "oldCallSign";
        final Long oldShipTypeId = 12356L;
        final String oldDestination = "oldDestination";
        final Timestamp oldEta = new Timestamp(oldTime);
        final Double oldDimensionToBowA = 2d;
        final Double oldDimensionToSternB = 1.3;
        final Double oldGenLength = oldDimensionToBowA + oldDimensionToSternB;
        final Double oldDimensionToPortC = 3d;
        final Double oldDimensionToStarboardD = 4d;
        final Double oldGenWidth = dimensionToPortC + dimensionToStarboardD;
        final Double oldDraught = 3d;
        final Timestamp oldPackageTime = new Timestamp(oldTime);
        final boolean deleted = false;

        var oldVessel = Vessel.builder()
                .imo(oldImo)
                .mmsi(oldMmsi)
                .mid(oldMid)
                .vesselName(oldVesselName)
                .callSign(oldCallSign)
                .shipTypeId(oldShipTypeId)
                .destination(oldDestination)
                .eta(oldEta)
                .dimensionToBowA(oldDimensionToBowA)
                .dimensionToSternB(oldDimensionToSternB)
                .genLength(oldGenLength)
                .dimensionToPortC(oldDimensionToPortC)
                .dimensionToStarboardD(oldDimensionToStarboardD)
                .genWidth(oldGenWidth)
                .draught(oldDraught)
                .packageTime(oldPackageTime)
                .deleted(deleted)
                .build();

        vesselMapper.updateVessel(oldVessel, vesselInfo);
        assertAll("Group vessel mapper assertions",
                () -> assertEquals(mmsi, oldVessel.getMmsi(), "Mmsi must to be updated!"),
                () -> assertEquals(vesselName, oldVessel.getVesselName(), "Vessel name must to be updated!"),
                () -> assertEquals(oldImo, oldVessel.getImo(), "Imo should not be updated!"),
                () -> assertEquals(callSign, oldVessel.getCallSign(), "Call sign must to be updated!"),
                () -> assertEquals(shipTypeId, oldVessel.getShipTypeId(), "Ship type id must to be updated!"),
                () -> assertEquals(destination, oldVessel.getDestination(), "Destination must to be updated!"),
                () -> assertEquals(eta.getNano(), oldVessel.getEta().getNanos(), "Eta must to be updated!"),
                () -> assertEquals(dimensionToBowA, oldVessel.getDimensionToBowA(), "Dimension To Bow A must to be updated!"),
                () -> assertEquals(dimensionToSternB, oldVessel.getDimensionToSternB(), "Dimension To Stern B must to be updated!"),
                () -> assertEquals(dimensionToPortC, oldVessel.getDimensionToPortC(), "Dimension To Port C must to be updated!"),
                () -> assertEquals(dimensionToStarboardD, oldVessel.getDimensionToStarboardD(), "Dimension To Starboard D must to be updated!"),
                () -> assertEquals(draught, oldVessel.getDraught(), "Draught must to be updated!"),
                () -> assertEquals(packageTime.getNano(), oldVessel.getPackageTime().getNanos(), "Package Time must to be updated!"),
                () -> assertFalse(oldVessel.isDeleted(), "Deleted should not be updated!"),
                () -> assertEquals(genLength, oldVessel.getGenLength(), "Gen length must to be updated!"),
                () -> assertEquals(genWidth, oldVessel.getGenWidth(), "Gen width must to be updated!"),
                () -> assertEquals(234L, oldVessel.getMid(), "Mid must to be updated!")
        );
    }
}