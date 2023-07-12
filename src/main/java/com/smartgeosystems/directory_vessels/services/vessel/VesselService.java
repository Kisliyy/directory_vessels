package com.smartgeosystems.directory_vessels.services.vessel;

import com.smartgeosystems.directory_vessels.dto.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.models.Vessel;
import org.vmts.vessel.VesselInfo;

public interface VesselService {

    void processingVessel(VesselInfo vesselInfo);

    Vessel processingVessel(VesselRequestDto vesselRequestDto);

    Vessel findByImo(long imo);

    Vessel findByMmsi(long mmsi);

    void updateVessel(VesselUpdateDto vesselUpdateDto);

    void deleteById(long imo);
}
