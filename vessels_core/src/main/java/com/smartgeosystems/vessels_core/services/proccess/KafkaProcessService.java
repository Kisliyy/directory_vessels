package com.smartgeosystems.vessels_core.services.proccess;

import com.smartgeosystems.utils.VesselUtils;
import com.smartgeosystems.vessels_core.mappers.vessels.VesselKafkaMapper;
import com.smartgeosystems.vessels_core.models.Vessel;
import com.smartgeosystems.vessels_core.services.vessels.VesselService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.vmts.vessel.VesselInfo;

import java.sql.SQLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProcessService implements ProcessService {

    private final VesselService vesselService;
    private final VesselKafkaMapper vesselKafkaMapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 2, value = SQLException.class, recover = "recoverVessel")
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
                Vessel persistVessel = vesselService.save(vessel);
                log.info("Add new vessel: {}", persistVessel);
                return;
            }
            var vessel = byId.get();
            updateVessel(vessel, vesselInfo);
        }
    }

    @Recover
    @Transactional
    public void recoverVessel(SQLException exception, VesselInfo vesselInfo) {
        var findVessel = vesselService.findByImo(vesselInfo.getImo());
        updateVessel(findVessel, vesselInfo);
    }

    private Vessel createNewVessel(VesselInfo vesselInfo) {
        return vesselKafkaMapper.vesselKafkaToVessel(vesselInfo);
    }

    private void updateVessel(Vessel vessel, VesselInfo vesselInfo) {
        if (!vessel.isDeleted()) {
            var packageTimeVesselInfo = vesselInfo.getPackageTime();
            var packageTimeVessel = vessel.getPackageTime();
            if (VesselUtils.checkPackageTime(packageTimeVessel, packageTimeVesselInfo)) {
                vesselKafkaMapper.updateVesselKafkaToVessel(vessel, vesselInfo);
                vesselService.save(vessel);
                log.info("The vessel has been updated: {}", vessel);
            }
        }
    }
}
