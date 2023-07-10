package com.smartgeosystems.directory_vessels.services.vessel;

import com.smartgeosystems.directory_vessels.dto.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.VesselResponseDto;
import com.smartgeosystems.directory_vessels.dto.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.exceptions.NotFoundException;
import com.smartgeosystems.directory_vessels.exceptions.VesselException;
import com.smartgeosystems.directory_vessels.mappers.VesselMapper;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.repository.VesselRepository;
import com.smartgeosystems.directory_vessels.utils.VesselUtils;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vmts.vessel.VesselInfo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VesselServiceImpl implements VesselService {

    private final VesselRepository vesselRepository;
    private final VesselMapper vesselMapper = Mappers.getMapper(VesselMapper.class);


    @Override
    public Vessel save(Vessel vessel) {
        return vesselRepository.save(vessel);
    }

    @Override
    @Transactional
    public void processingVessel(VesselInfo vesselInfo) {
        var mmsi = vesselInfo.getMmsi();
        if (VesselUtils.isAton(mmsi)) {
            return;
        }
        var imo = vesselInfo.getImo();
        if (imo != null) {
            Optional<Vessel> byId = vesselRepository.findByImo(imo);
            if (byId.isEmpty()) {
                Vessel vessel = createNewVessel(vesselInfo);
                vesselRepository.save(vessel);
                return;
            }
            Vessel vessel = byId.get();
            updateVessel(vessel, vesselInfo);
        }
    }

    @Override
    public VesselResponseDto processingVessel(VesselRequestDto vesselRequestDto) {
        if (VesselUtils.isAton(vesselRequestDto.getMmsi())) {
            throw new VesselException("The data obtained indicate that this is aton");
        }
        var imo = vesselRequestDto.getImo();
        if (vesselRepository.existsById(imo)) {
            String message = String.format("A vessel with such imo %s exists", imo);
            throw new VesselException(message);
        }
        Vessel vessel = vesselMapper.vesselRequestDtoToVessel(vesselRequestDto);
        Vessel persistVessel = vesselRepository.save(vessel);
        return vesselMapper.vesselToVesselResponseDto(persistVessel);
    }

    @Override
    public VesselResponseDto findByImo(long imo) {
        return vesselRepository
                .findByImo(imo)
                .map(vesselMapper::vesselToVesselResponseDto)
                .orElseThrow(() -> new NotFoundException("Vessel not found by imo: " + imo));
    }

    @Override
    public VesselResponseDto findByMmsi(long mmsi) {
        return vesselRepository
                .findByImo(mmsi)
                .map(vesselMapper::vesselToVesselResponseDto)
                .orElseThrow(() -> new NotFoundException("Vessel not found by mmsi: " + mmsi));
    }

    @Override
    @Transactional
    public void updateVessel(VesselUpdateDto vesselUpdateDto) {
        var imo = vesselUpdateDto.getImo();
        Optional<Vessel> byImo = vesselRepository.findByImo(imo);
        if (byImo.isPresent()) {
            var vessel = byImo.get();
            if (VesselUtils.checkPackageTime(vessel.getPackageTime(), vesselUpdateDto.getPackageTime())) {
                vesselMapper.updateVessel(vessel, vesselUpdateDto);
            }
        }
        Vessel vessel = vesselMapper.vesselUpdateDtoToVessel(vesselUpdateDto);
        save(vessel);
    }

    @Override
    @Transactional
    public void deleteById(long imo) {
        if (vesselRepository.existsById(imo)) {
            vesselRepository.updateDelete(imo, true);
        }
    }

    private void updateVessel(Vessel vessel, VesselInfo vesselInfo) {
        var packageTimeVessel = vessel.getPackageTime();
        if (VesselUtils.checkPackageTime(packageTimeVessel, vesselInfo.getPackageTime())) {
            vesselMapper.updateVesselKafkaToVessel(vessel, vesselInfo);
        }
    }

    private Vessel createNewVessel(VesselInfo vesselInfo) {
        return vesselMapper.vesselKafkaToVessel(vesselInfo);
    }


}
