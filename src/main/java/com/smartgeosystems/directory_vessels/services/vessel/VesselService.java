package com.smartgeosystems.directory_vessels.services.vessel;

import com.smartgeosystems.directory_vessels.dto.VesselKafkaDto;
import com.smartgeosystems.directory_vessels.models.Vessel;

import java.util.Optional;

public interface VesselService {
    Optional<Vessel> findByMmsi(Long mmsi);

    Vessel save(Vessel vessel);

    void processingVessel(VesselKafkaDto vesselKafkaDto);
}
