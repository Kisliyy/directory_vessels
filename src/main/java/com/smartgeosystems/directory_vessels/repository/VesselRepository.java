package com.smartgeosystems.directory_vessels.repository;

import com.smartgeosystems.directory_vessels.models.Vessel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VesselRepository extends JpaRepository<Vessel, Long> {
    Optional<Vessel> findByMmsi(Long mmsi);
}
