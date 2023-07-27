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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VesselServiceImpl implements VesselService {

    private final VesselRepository vesselRepository;
    private final VesselMapper vesselMapper;

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "vessels_imo", key = "#vesselRequestDto.imo"),
            @CachePut(value = "vessels_mmsi", key = "#vesselRequestDto.mmsi")
    })
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
    @Cacheable(value = "vessels_imo")
    public Vessel findByImo(long imo) {
        return vesselRepository
                .findByImo(imo)
                .orElseThrow(() -> new NotFoundException("Vessel not found by imo: " + imo));
    }

    @Override
    @Cacheable(value = "vessels_imo")
    public Optional<Vessel> getByImo(long imo) {
        return vesselRepository
                .findById(imo);
    }

    @Override
    @Cacheable(value = "vessels_mmsi")
    public Vessel findByMmsi(long mmsi) {
        return vesselRepository
                .findByMmsi(mmsi)
                .orElseThrow(() -> new NotFoundException("Vessel not found by mmsi: " + mmsi));
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "vessels_imo", key = "#vesselUpdateDto.imo"),
            @CachePut(value = "vessels_mmsi", key = "#vesselUpdateDto.mmsi")
    })
    public Vessel updateVessel(VesselUpdateDto vesselUpdateDto) {
        var imo = vesselUpdateDto.getImo();
        Optional<Vessel> byImo = vesselRepository.findById(imo);
        if (byImo.isPresent()) {
            var vessel = byImo.get();
            if (!vessel.isDeleted()) {
                if (VesselUtils.checkPackageTime(vessel.getPackageTime(), vesselUpdateDto.getPackageTime())) {
                    vesselMapper.updateVessel(vessel, vesselUpdateDto);
                    log.info("The vessel has been updated: {}", vessel);
                    return vessel;
                }
                throw new VesselException("Error when comparing package time");
            }
            throw new VesselException("The vessel you want to upgrade has been deleted");
        }
        Vessel vessel = vesselMapper.vesselUpdateDtoToVessel(vesselUpdateDto);
        Vessel persistVessel = vesselRepository.save(vessel);
        log.info("Add new vessel: {}", persistVessel);
        return persistVessel;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "vessels_imo", key = "#imo"),
            @CacheEvict(value = "vessels_mmsi", allEntries = true)
    })
    public void deleteById(long imo) {
        if (vesselRepository.existsById(imo)) {
            vesselRepository.updateDelete(imo, true);
            log.info("Vessel deleted with imo: {}", imo);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Caching(put = {
            @CachePut(value = "vessels_imo", key = "#vessel.imo"),
            @CachePut(value = "vessels_mmsi", key = "#vessel.mmsi")
    })
    public Vessel save(Vessel vessel) {
        return vesselRepository.save(vessel);
    }

    @Override
    public Page<Vessel> findAll(Pageable page) {
        return vesselRepository
                .findAll(page);
    }

    @Override
    public List<Vessel> findByDestination(String destination) {
        return vesselRepository
                .findByDestination(destination);
    }

}
