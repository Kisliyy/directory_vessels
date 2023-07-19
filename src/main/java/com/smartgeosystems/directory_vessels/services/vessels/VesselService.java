package com.smartgeosystems.directory_vessels.services.vessels;

import com.smartgeosystems.directory_vessels.dto.vessels.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.vessels.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.models.Vessel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vmts.vessel.VesselInfo;

public interface VesselService {

    void processingVessel(VesselInfo vesselInfo);

    Vessel processingVessel(VesselRequestDto vesselRequestDto);

    Vessel findByImo(long imo);

    Vessel findByMmsi(long mmsi);

    void updateVessel(VesselUpdateDto vesselUpdateDto);

    void deleteById(long imo);

    Page<Vessel> findAll(Pageable page);

}
