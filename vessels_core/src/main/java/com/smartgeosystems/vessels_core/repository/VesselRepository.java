package com.smartgeosystems.vessels_core.repository;

import com.smartgeosystems.vessels_core.models.Vessel;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VesselRepository extends JpaRepository<Vessel, Long> {

    @Where(clause = "deleted = false")
    Optional<Vessel> findByImo(long imo);

    @Where(clause = "deleted = false")
    Optional<Vessel> findByMmsi(long mmsi);

    @Modifying
    @Query(value = "UPDATE Vessel as v SET v.deleted = :deleted WHERE v.imo = :imo")
    void updateDelete(@Param("imo") long imo, @Param("deleted") boolean deleted);

    @Where(clause = "deleted = false")
    List<Vessel> findByDestination(String destination);
}
