package com.smartgeosystems.directory_vessels.services.vessel;

import com.smartgeosystems.directory_vessels.dto.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.exceptions.NotFoundException;
import com.smartgeosystems.directory_vessels.exceptions.VesselException;
import com.smartgeosystems.directory_vessels.mappers.VesselMapper;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.repository.VesselRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {VesselServiceImpl.class, VesselMapper.class})
class VesselServiceImplTest {

    @MockBean
    private VesselRepository vesselRepository;

    @MockBean
    private VesselMapper vesselMapper;

    @Autowired
    private VesselService vesselService;


    private final Long mmsi = 111234567L;
    private final String vesselName = "vesselName";
    private final String callSign = "callSign";
    private final long imo = 1234567L;
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

    private Timestamp packageTimeTimestamp;

    private Timestamp etaTimestamp;

    private Timestamp creationTimestamp;

    private VesselInfo vesselInfo;

    private Vessel vessel;

    private Vessel persistVessel;

    @BeforeEach
    void init() {
        packageTimeTimestamp = Timestamp.from(packageTime);
        etaTimestamp = Timestamp.from(eta);
        creationTimestamp = Timestamp.from(Instant.now());

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
        when(vesselRepository.findByImo(anyLong()))
                .thenReturn(Optional.empty());

        when(vesselMapper.vesselKafkaToVessel(any(VesselInfo.class)))
                .thenReturn(vessel);

        when(vesselRepository.save(vessel))
                .thenReturn(persistVessel);

        vesselService.processingVessel(vesselInfo);

        verify(vesselRepository, times(1)).findByImo(anyLong());
        verify(vesselRepository, times(1)).save(vessel);
        verify(vesselMapper, times(1)).vesselKafkaToVessel(any(VesselInfo.class));
    }

    @Test
    void processingVesselKafkaIsAtonTest() {
        var mmsi = 999123456L;
        VesselInfo vesselInfo = new VesselInfo();
        vesselInfo.setMmsi(mmsi);

        vesselService.processingVessel(vesselInfo);

        verify(vesselRepository, times(0)).findByImo(anyLong());
        verify(vesselRepository, times(0)).save(any());
        verify(vesselMapper, times(0)).vesselKafkaToVessel(any());
    }

    @Test
    void processingVesselKafkaUpdateVesselWhereOldPackageTimeTest() {
        when(vesselRepository.findByImo(vesselInfo.getImo()))
                .thenReturn(Optional.of(persistVessel));

        vesselService.processingVessel(vesselInfo);


        verify(vesselRepository, times(1)).findByImo(vesselInfo.getImo());
        verify(vesselRepository, times(0)).save(any());
        verify(vesselMapper, times(0)).updateVesselKafkaToVessel(persistVessel, vesselInfo);
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

        when(vesselRepository.findByImo(vesselInfo.getImo()))
                .thenReturn(Optional.of(oldVessel));

        doNothing()
                .when(vesselMapper)
                .updateVesselKafkaToVessel(oldVessel, vesselInfo);

        vesselService.processingVessel(vesselInfo);

        verify(vesselRepository, times(1)).findByImo(vesselInfo.getImo());
        verify(vesselRepository, times(0)).save(any());
    }


    @Test
    void processingVesselKafkaWhereImoIsNullTest() {
        Long imo = null;
        VesselInfo vesselInfo = new VesselInfo();
        vesselInfo.setImo(imo);

        vesselService.processingVessel(vesselInfo);

        verify(vesselRepository, times(0)).findByImo(anyLong());
        verify(vesselRepository, times(0)).save(any());
    }


    @Test
    void processingVesselRequestDtoIsAtonTest() {
        var mmsi = 999123456L;
        VesselRequestDto vesselRequestDto = new VesselRequestDto();
        vesselRequestDto.setMmsi(mmsi);

        assertThrows(VesselException.class, () -> vesselService.processingVessel(vesselRequestDto));

        verify(vesselRepository, times(0)).existsById(anyLong());
        verify(vesselMapper, times(0)).vesselRequestDtoToVessel(any());
        verify(vesselRepository, times(0)).save(any());
    }

    @Test
    void processingVesselRequestDtoIfVesselExistTest() {
        var imo = 999123456L;
        VesselRequestDto vesselRequestDto = new VesselRequestDto();
        vesselRequestDto.setImo(imo);

        when(vesselRepository.existsById(imo))
                .thenReturn(true);

        assertThrows(VesselException.class, () -> vesselService.processingVessel(vesselRequestDto));

        verify(vesselRepository, times(1)).existsById(imo);
        verify(vesselMapper, times(0)).vesselRequestDtoToVessel(any());
        verify(vesselRepository, times(0)).save(any());
    }


    @Test
    void processingVesselRequestDtoToVesselTest() {
        var vesselRequestDto = VesselRequestDto.builder()
                .imo(imo)
                .mmsi(mmsi)
                .vesselName(vesselName)
                .callSign(callSign)
                .shipTypeId(shipTypeId)
                .destination(destination)
                .eta(etaTimestamp)
                .dimensionToBowA(dimensionToBowA)
                .dimensionToSternB(dimensionToSternB)
                .dimensionToPortC(dimensionToPortC)
                .dimensionToStarboardD(dimensionToStarboardD)
                .draught(draught)
                .packageTime(packageTimeTimestamp)
                .build();

        when(vesselRepository.existsById(vesselRequestDto.getImo()))
                .thenReturn(false);

        when(vesselMapper.vesselRequestDtoToVessel(vesselRequestDto))
                .thenReturn(vessel);

        when(vesselRepository.save(vessel))
                .thenReturn(persistVessel);

        Vessel newVessel = vesselService.processingVessel(vesselRequestDto);

        assertNotNull(newVessel);
        assertEquals(persistVessel, newVessel);

        verify(vesselRepository, times(1)).existsById(vesselRequestDto.getImo());
        verify(vesselMapper, times(1)).vesselRequestDtoToVessel(vesselRequestDto);
        verify(vesselRepository, times(1)).save(vessel);
    }

    @Test
    void findByImoSuccessfulTest() {
        var imo = 1234L;

        when(vesselRepository.findByImo(imo))
                .thenReturn(Optional.of(new Vessel()));

        vesselService.findByImo(imo);

        verify(vesselRepository, times(1)).findByImo(imo);
    }

    @Test
    void findByImoReturnExceptionTest() {
        when(vesselRepository.findByImo(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vesselService.findByImo(anyLong()));

        verify(vesselRepository, times(1)).findByImo(anyLong());
    }


    @Test
    void findByMmsiSuccessfulTest() {
        var mmsi = 1234L;

        when(vesselRepository.findByMmsi(mmsi))
                .thenReturn(Optional.of(new Vessel()));

        vesselService.findByMmsi(mmsi);

        verify(vesselRepository, times(1)).findByMmsi(mmsi);
    }

    @Test
    void findByMmsiReturnExceptionTest() {
        when(vesselRepository.findByMmsi(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vesselService.findByMmsi(anyLong()));

        verify(vesselRepository, times(1)).findByMmsi(anyLong());
    }

    @Test
    void deleteIfVesselExistTest() {
        var imo = 1234L;

        when(vesselRepository.existsById(imo))
                .thenReturn(true);

        doNothing()
                .when(vesselRepository)
                .deleteById(imo);

        vesselService.deleteById(imo);

        verify(vesselRepository, times(1)).existsById(imo);
        verify(vesselRepository, times(1)).updateDelete(imo, true);
    }

    @Test
    void deleteIfVesselIsNotExistTest() {
        when(vesselRepository.existsById(anyLong()))
                .thenReturn(false);

        doNothing()
                .when(vesselRepository)
                .deleteById(anyLong());

        vesselService.deleteById(anyLong());

        verify(vesselRepository, times(1)).existsById(anyLong());
        verify(vesselRepository, times(0)).updateDelete(anyLong(), anyBoolean());
    }


    @Test
    void updateVesselIfVesselExistAndPackageTimeIsEqualTest() {
        var vesselUpdateDto = VesselUpdateDto.builder()
                .imo(imo)
                .mmsi(mmsi)
                .vesselName(vesselName)
                .callSign(callSign)
                .shipTypeId(shipTypeId)
                .destination(destination)
                .eta(etaTimestamp)
                .dimensionToBowA(dimensionToBowA)
                .dimensionToSternB(dimensionToSternB)
                .dimensionToPortC(dimensionToPortC)
                .dimensionToStarboardD(dimensionToStarboardD)
                .draught(draught)
                .packageTime(packageTimeTimestamp)
                .build();

        when(vesselRepository.findByImo(vesselUpdateDto.getImo()))
                .thenReturn(Optional.of(persistVessel));

        vesselService.updateVessel(vesselUpdateDto);

        verify(vesselRepository, times(1)).findByImo(vesselUpdateDto.getImo());
        verify(vesselMapper, times(0)).updateVessel(any(), any());
        verify(vesselMapper, times(0)).vesselUpdateDtoToVessel(any());
        verify(vesselRepository, times(0)).save(any());
    }


    @Test
    void updateVesselIfVesselExistAndNewPackageTimeYoungerTest() {
        Date date = new Date();
        var updatePackageTime = new Timestamp(date.getTime() + 100000);

        var vesselUpdateDto = VesselUpdateDto.builder()
                .imo(imo)
                .mmsi(mmsi)
                .vesselName(vesselName)
                .callSign(callSign)
                .shipTypeId(shipTypeId)
                .destination(destination)
                .eta(etaTimestamp)
                .dimensionToBowA(dimensionToBowA)
                .dimensionToSternB(dimensionToSternB)
                .dimensionToPortC(dimensionToPortC)
                .dimensionToStarboardD(dimensionToStarboardD)
                .draught(draught)
                .packageTime(updatePackageTime)
                .build();

        when(vesselRepository.findByImo(vesselUpdateDto.getImo()))
                .thenReturn(Optional.of(persistVessel));

        doNothing()
                .when(vesselMapper)
                .updateVessel(persistVessel, vesselUpdateDto);


        vesselService.updateVessel(vesselUpdateDto);

        verify(vesselRepository, times(1)).findByImo(vesselUpdateDto.getImo());
        verify(vesselMapper, times(1)).updateVessel(persistVessel, vesselUpdateDto);
        verify(vesselMapper, times(0)).vesselUpdateDtoToVessel(any());
        verify(vesselRepository, times(0)).save(any());
    }


    @Test
    void updateVesselIfVesselIsNotExist() {
        Date date = new Date();
        var updatePackageTime = new Timestamp(date.getTime() + 100000);

        var vesselUpdateDto = VesselUpdateDto.builder()
                .imo(imo)
                .mmsi(mmsi)
                .vesselName(vesselName)
                .callSign(callSign)
                .shipTypeId(shipTypeId)
                .destination(destination)
                .eta(etaTimestamp)
                .dimensionToBowA(dimensionToBowA)
                .dimensionToSternB(dimensionToSternB)
                .dimensionToPortC(dimensionToPortC)
                .dimensionToStarboardD(dimensionToStarboardD)
                .draught(draught)
                .packageTime(updatePackageTime)
                .build();

        when(vesselRepository.findByImo(vesselUpdateDto.getImo()))
                .thenReturn(Optional.empty());

        when(vesselMapper.vesselUpdateDtoToVessel(vesselUpdateDto))
                .thenReturn(vessel);

        when(vesselRepository.save(vessel))
                .thenReturn(persistVessel);

        vesselService.updateVessel(vesselUpdateDto);

        verify(vesselRepository, times(1)).findByImo(vesselUpdateDto.getImo());
        verify(vesselMapper, times(0)).updateVessel(any(), any());
        verify(vesselMapper, times(1)).vesselUpdateDtoToVessel(vesselUpdateDto);
        verify(vesselRepository, times(1)).save(vessel);
    }
}