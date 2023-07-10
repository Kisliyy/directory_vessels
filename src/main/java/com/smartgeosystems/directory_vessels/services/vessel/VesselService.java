package com.smartgeosystems.directory_vessels.services.vessel;

import com.smartgeosystems.directory_vessels.dto.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.VesselResponseDto;
import com.smartgeosystems.directory_vessels.dto.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.models.Vessel;
import org.vmts.vessel.VesselInfo;

public interface VesselService {

    Vessel save(Vessel vessel);

    void processingVessel(VesselInfo vesselInfo);

    VesselResponseDto processingVessel(VesselRequestDto vesselRequestDto);

    VesselResponseDto findByImo(long imo);

    VesselResponseDto findByMmsi(long mmsi);

    void updateVessel(VesselUpdateDto vesselUpdateDto);

    void deleteById(long imo);
}
