package com.smartgeosystems.vessels_core.services.vessels;

import com.smartgeosystems.vessels_core.dto.vessels.VesselRequestDto;
import com.smartgeosystems.vessels_core.dto.vessels.VesselUpdateDto;
import com.smartgeosystems.vessels_core.models.Vessel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VesselService {

    Vessel processingVessel(VesselRequestDto vesselRequestDto);

    Vessel findByImo(long imo);

    Optional<Vessel> getByImo(long imo);

    Vessel findByMmsi(long mmsi);

    Vessel updateVessel(VesselUpdateDto vesselUpdateDto);

    void deleteById(long imo);

    Vessel save(Vessel vessel);

    Page<Vessel> findAll(Pageable page);

    List<Vessel> findByDestination(String destination);
}
