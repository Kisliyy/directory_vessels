package com.smartgeosystems.directory_vessels.mappers.vessels;

import com.smartgeosystems.directory_vessels.models.Vessel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vmts.vessel.VesselInfo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class VesselKafkaMapperTest {

    private final VesselKafkaMapper vesselKafkaMapper = Mappers.getMapper(VesselKafkaMapper.class);

    private final Long mmsi = 111234567L;
    private final String vesselName = "vesselName";
    private final String callSign = "callSign";
    private final Long imo = 1234567L;
    private final Long shipTypeId = 123L;
    private final String destination = "destination";
    private final Instant eta = Instant.now();
    private final Double dimensionToBowA = 1.2;
    private final Double dimensionToSternB = 1.4;
    private final Double genLength = dimensionToBowA + dimensionToSternB;
    private final Double dimensionToPortC = 1.6;
    private final Double dimensionToStarboardD = 1.8;
    private final Double genWidth = dimensionToPortC + dimensionToStarboardD;
    private final Double draught = 2.4;
    private final Instant packageTime = Instant.now();
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
        Vessel vessel = vesselKafkaMapper.vesselKafkaToVessel(vesselInfo);
        assertAll("Group vessel mapper assertions",
                () -> assertEquals(mmsi, vessel.getMmsi(), "Mmsi must be moved!"),
                () -> assertEquals(vesselName, vessel.getVesselName(), "Vessel name must be moved!"),
                () -> assertEquals(callSign, vessel.getCallSign(), "Call sign must be moved!"),
                () -> assertEquals(imo, vessel.getImo(), "Imo must be moved!"),
                () -> assertEquals(shipTypeId, vessel.getShipTypeId(), "Ship type id must be moved!"),
                () -> assertEquals(destination, vessel.getDestination(), "Destination must be moved!"),
                () -> assertEquals(eta.getNano(), vessel.getEta().getNanos(), "Eta must be moved!"),
                () -> assertEquals(dimensionToBowA, vessel.getDimensionToBowA(), "Dimension To Bow A must be moved!"),
                () -> assertEquals(dimensionToSternB, vessel.getDimensionToSternB(), "Dimension To Stern B must be moved!"),
                () -> assertEquals(dimensionToPortC, vessel.getDimensionToPortC(), "Dimension To Port C must be moved!"),
                () -> assertEquals(dimensionToStarboardD, vessel.getDimensionToStarboardD(), "Dimension To Starboard D must be moved!"),
                () -> assertEquals(draught, vessel.getDraught(), "Draught must be moved!"),
                () -> assertEquals(packageTime.getNano(), vessel.getPackageTime().getNanos(), "Package Time must be moved!"),
                () -> assertFalse(vessel.isDeleted(), "Deleted must be default is false"),
                () -> assertEquals(genLength, vessel.getGenLength(), "Gen length must be moved!"),
                () -> assertEquals(genWidth, vessel.getGenWidth(), "Gen width must be moved!"),
                () -> assertEquals(234L, vessel.getMid(), "Mid must be equal to expected value")
        );
    }


    @Test
    void updatePersistVesselFromVesselInfoSuccessfulMappingTest() {
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
        final Timestamp oldCreationTime = new Timestamp(oldTime);
        final Timestamp oldUpdateTime = new Timestamp(oldTime + 1000);
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
                .creationTime(oldCreationTime)
                .updateTime(oldUpdateTime)
                .deleted(deleted)
                .build();

        vesselKafkaMapper.updateVesselKafkaToVessel(oldVessel, vesselInfo);
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
                () -> assertEquals(234L, oldVessel.getMid(), "Mid must to be updated!"),
                () -> assertEquals(oldCreationTime, oldVessel.getCreationTime(), "Creation time should not updated!"),
                () -> assertEquals(oldUpdateTime, oldVessel.getUpdateTime(), "Update time should not updated!")
        );
    }

}