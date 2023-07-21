package com.smartgeosystems.directory_vessels.services.vessels;

import com.smartgeosystems.directory_vessels.dto.vessels.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.vessels.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.exceptions.NotFoundException;
import com.smartgeosystems.directory_vessels.exceptions.VesselException;
import com.smartgeosystems.directory_vessels.mappers.vessels.VesselMapper;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.repository.VesselRepository;
import com.smartgeosystems.directory_vessels.utils.VesselUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VesselServiceImpl implements VesselService {

    private final VesselRepository vesselRepository;
    private final VesselMapper vesselMapper;

    @Override
    @Transactional
    public Vessel processingVessel(VesselRequestDto vesselRequestDto) {
        if (VesselUtils.isAton(vesselRequestDto.getMmsi())) {
            throw new VesselException("The data obtained indicate that this is aton");
        }
        var imo = vesselRequestDto.getImo();
        if (vesselRepository.existsById(imo)) {
            String message = String.format("A vessel with such imo %s exists", imo);
            throw new VesselException(message);
        }
        Vessel vessel = vesselMapper.vesselRequestDtoToVessel(vesselRequestDto);
        return vesselRepository.save(vessel);
    }

    @Override
    public Vessel findByImo(long imo) {
        return vesselRepository
                .findByImo(imo)
                .orElseThrow(() -> new NotFoundException("Vessel not found by imo: " + imo));
    }

    @Override
    public Optional<Vessel> getByImo(long imo) {
        return vesselRepository
                .findByImo(imo);
    }

    @Override
    public Vessel findByMmsi(long mmsi) {
        return vesselRepository
                .findByMmsi(mmsi)
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
                log.info("The vessel has been updated: {}", vessel);
            }
            return;
        }
        Vessel vessel = vesselMapper.vesselUpdateDtoToVessel(vesselUpdateDto);
        Vessel persistVessel = vesselRepository.save(vessel);
        log.info("Add new vessel: {}", persistVessel);
    }

    @Override
    @Transactional
    public void deleteById(long imo) {
        if (vesselRepository.existsById(imo)) {
            vesselRepository.updateDelete(imo, true);
            log.info("Vessel deleted with imo: {}", imo);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Vessel save(Vessel vessel) {
        return vesselRepository.save(vessel);
    }

    @Override
    public Page<Vessel> findAll(Pageable page) {
        return vesselRepository
                .findAll(page);
    }

}
