package com.smartgeosystems.directory_vessels.services.proccess;

import com.smartgeosystems.directory_vessels.mappers.vessels.VesselKafkaMapper;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.services.vessels.VesselService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.vmts.vessel.VesselInfo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = KafkaProcessService.class)
class KafkaProcessServiceTest {

    @Autowired
    private ProcessService processService;

    @MockBean
    private VesselService vesselService;

    @MockBean
    private VesselKafkaMapper vesselKafkaMapper;


    private final Long mmsi = 111234567L;
    private final long imo = 1234567L;
    private final Instant eta = Instant.now();
    private final Double dimensionToBowA = 1.2;
    private final Double dimensionToSternB = 1.4;
    private final Double genLength = dimensionToBowA + dimensionToSternB;
    private final Double dimensionToPortC = 1.6;
    private final Double dimensionToStarboardD = 1.8;
    private final Double genWidth = dimensionToPortC + dimensionToStarboardD;
    private final Double draught = 2.4;
    private final Instant packageTime = Instant.now();

    private Timestamp creationTimestamp;

    private VesselInfo vesselInfo;

    private Vessel vessel;

    private Vessel persistVessel;

    @BeforeEach
    void init() {
        Timestamp packageTimeTimestamp = Timestamp.from(packageTime);
        Timestamp etaTimestamp = Timestamp.from(eta);
        creationTimestamp = Timestamp.from(Instant.now());

        String destination = "destination";
        Long shipTypeId = 123L;
        String vesselName = "vesselName";
        String callSign = "callSign";
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

        vessel = Vessel.builder()
                .mmsi(mmsi)
                .vesselName(vesselName)
                .callSign(callSign)
                .imo(imo)
                .shipTypeId(shipTypeId)
                .destination(destination)
                .genLength(genLength)
                .genWidth(genWidth)
                .eta(etaTimestamp)
                .deleted(false)
                .dimensionToBowA(dimensionToBowA)
                .dimensionToSternB(dimensionToSternB)
                .dimensionToPortC(dimensionToPortC)
                .dimensionToStarboardD(dimensionToStarboardD)
                .packageTime(packageTimeTimestamp)
                .draught(draught)
                .build();

        persistVessel = Vessel.builder()
                .mmsi(mmsi)
                .vesselName(vesselName)
                .callSign(callSign)
                .imo(imo)
                .shipTypeId(shipTypeId)
                .genLength(genLength)
                .genWidth(genWidth)
                .destination(destination)
                .eta(etaTimestamp)
                .deleted(false)
                .dimensionToBowA(dimensionToBowA)
                .dimensionToSternB(dimensionToSternB)
                .dimensionToPortC(dimensionToPortC)
                .dimensionToStarboardD(dimensionToStarboardD)
                .packageTime(packageTimeTimestamp)
                .draught(draught)
                .creationTime(creationTimestamp)
                .build();
    }


    @Test
    void processingVesselKafkaCreateVesselTest() {
        when(vesselService.getByImo(anyLong()))
                .thenReturn(Optional.empty());

        when(vesselKafkaMapper.vesselKafkaToVessel(any(VesselInfo.class)))
                .thenReturn(vessel);

        when(vesselService.save(vessel))
                .thenReturn(persistVessel);

        processService.process(vesselInfo);

        verify(vesselService, times(1)).getByImo(anyLong());
        verify(vesselService, times(1)).save(vessel);
        verify(vesselKafkaMapper, times(1)).vesselKafkaToVessel(any(VesselInfo.class));
    }

    @Test
    void processingVesselKafkaIsAtonTest() {
        var mmsi = 999123456L;
        VesselInfo vesselInfo = new VesselInfo();
        vesselInfo.setMmsi(mmsi);

        processService.process(vesselInfo);

        verify(vesselService, times(0)).getByImo(anyLong());
        verify(vesselService, times(0)).save(any());
        verify(vesselKafkaMapper, times(0)).vesselKafkaToVessel(any());
    }

    @Test
    void processingVesselKafkaUpdateVesselWhereOldPackageTimeTest() {
        when(vesselService.getByImo(vesselInfo.getImo()))
                .thenReturn(Optional.of(persistVessel));

        processService.process(vesselInfo);


        verify(vesselService, times(1)).getByImo(vesselInfo.getImo());
        verify(vesselService, times(0)).save(any());
        verify(vesselKafkaMapper, times(0)).updateVesselKafkaToVessel(persistVessel, vesselInfo);
    }


    @Test
    void processingVesselKafkaUpdateVesselWhereNewPackageTimeTest() {
        Date date = new Date();
        var oldPackageTime = new Timestamp(date.getTime() - 10000);

        var oldVessel = Vessel.builder()
                .mmsi(mmsi)
                .imo(imo)
                .deleted(false)
                .packageTime(oldPackageTime)
                .creationTime(creationTimestamp)
                .draught(draught)
                .build();

        when(vesselService.getByImo(vesselInfo.getImo()))
                .thenReturn(Optional.of(oldVessel));

        doNothing()
                .when(vesselKafkaMapper)
                .updateVesselKafkaToVessel(oldVessel, vesselInfo);

        processService.process(vesselInfo);

        verify(vesselService, times(1)).getByImo(vesselInfo.getImo());
        verify(vesselService, times(0)).save(any());
    }


    @Test
    void processingVesselKafkaWhereImoIsNullTest() {
        Long imo = null;
        VesselInfo vesselInfo = new VesselInfo();
        vesselInfo.setImo(imo);

        processService.process(vesselInfo);

        verify(vesselService, times(0)).getByImo(anyLong());
        verify(vesselService, times(0)).save(any());
    }
}