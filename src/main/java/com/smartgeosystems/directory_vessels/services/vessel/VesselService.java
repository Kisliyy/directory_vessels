package com.smartgeosystems.directory_vessels.services.vessel;

import com.smartgeosystems.directory_vessels.models.Vessel;
import org.vmts.vessel.VesselInfo;

import java.util.Optional;

public interface VesselService {
    Optional<Vessel> findByMmsi(Long mmsi);

    Vessel save(Vessel vessel);

    void processingVessel(VesselInfo vesselInfo);
}
