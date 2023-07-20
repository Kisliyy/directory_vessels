package com.smartgeosystems.directory_vessels.services.proccess;

import com.smartgeosystems.directory_vessels.mappers.vessels.VesselKafkaMapper;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.services.vessels.VesselService;
import com.smartgeosystems.directory_vessels.utils.VesselUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vmts.vessel.VesselInfo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaProcessService implements ProcessService {

    private final VesselService vesselService;
    private final VesselKafkaMapper vesselKafkaMapper;

    @Override
    @Transactional
    public void process(VesselInfo vesselInfo) {
        var mmsi = vesselInfo.getMmsi();
        if (VesselUtils.isAton(mmsi)) {
            return;
        }
        var imo = vesselInfo.getImo();
        if (imo != null) {
            Optional<Vessel> byId = vesselService.getByImo(imo);
            if (byId.isEmpty()) {
                Vessel vessel = createNewVessel(vesselInfo);
                vesselService.save(vessel);
                return;
            }
            Vessel vessel = byId.get();
            updateVessel(vessel, vesselInfo);
        }
    }

    private Vessel createNewVessel(VesselInfo vesselInfo) {
        return vesselKafkaMapper.vesselKafkaToVessel(vesselInfo);
    }

    private void updateVessel(Vessel vessel, VesselInfo vesselInfo) {
        var packageTimeVesselInfo = vesselInfo.getPackageTime();
        var packageTimeVessel = vessel.getPackageTime();
        if (VesselUtils.checkPackageTime(packageTimeVessel, packageTimeVesselInfo)) {
            vesselKafkaMapper.updateVesselKafkaToVessel(vessel, vesselInfo);
        }
    }
}
