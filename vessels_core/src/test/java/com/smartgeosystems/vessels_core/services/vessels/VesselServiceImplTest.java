package com.smartgeosystems.vessels_core.services.vessels;

import com.smartgeosystems.vessels_core.dto.vessels.VesselRequestDto;
import com.smartgeosystems.vessels_core.dto.vessels.VesselUpdateDto;
import com.smartgeosystems.vessels_core.exceptions.NotFoundException;
import com.smartgeosystems.vessels_core.exceptions.VesselException;
import com.smartgeosystems.vessels_core.mappers.vessels.VesselMapper;
import com.smartgeosystems.vessels_core.models.Vessel;
import com.smartgeosystems.vessels_core.repository.VesselRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    private Vessel vessel;

    private Vessel persistVessel;

    @BeforeEach
    void init() {
        packageTimeTimestamp = Timestamp.from(packageTime);
        etaTimestamp = Timestamp.from(eta);
        Timestamp creationTimestamp = Timestamp.from(Instant.now());

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

        when(vesselRepository.findById(vesselUpdateDto.getImo()))
                .thenReturn(Optional.of(persistVessel));

        assertThrows(VesselException.class, () -> vesselService.updateVessel(vesselUpdateDto));

        verify(vesselRepository, times(1)).findById(vesselUpdateDto.getImo());
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

        when(vesselRepository.findById(vesselUpdateDto.getImo()))
                .thenReturn(Optional.of(persistVessel));

        doNothing()
                .when(vesselMapper)
                .updateVessel(persistVessel, vesselUpdateDto);


        vesselService.updateVessel(vesselUpdateDto);

        verify(vesselRepository, times(1)).findById(vesselUpdateDto.getImo());
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

        when(vesselRepository.findById(vesselUpdateDto.getImo()))
                .thenReturn(Optional.empty());

        when(vesselMapper.vesselUpdateDtoToVessel(vesselUpdateDto))
                .thenReturn(vessel);

        when(vesselRepository.save(vessel))
                .thenReturn(persistVessel);

        vesselService.updateVessel(vesselUpdateDto);

        verify(vesselRepository, times(1)).findById(vesselUpdateDto.getImo());
        verify(vesselMapper, times(0)).updateVessel(any(), any());
        verify(vesselMapper, times(1)).vesselUpdateDtoToVessel(vesselUpdateDto);
        verify(vesselRepository, times(1)).save(vessel);
    }
}