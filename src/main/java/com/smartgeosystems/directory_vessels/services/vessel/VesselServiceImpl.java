package com.smartgeosystems.directory_vessels.services.vessel;

import com.smartgeosystems.directory_vessels.mappers.VesselMapper;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.repository.VesselRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vmts.vessel.VesselInfo;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VesselServiceImpl implements VesselService {

    private final VesselRepository vesselRepository;
    private final VesselMapper vesselMapper = Mappers.getMapper(VesselMapper.class);

    @Override
    public Optional<Vessel> findByMmsi(Long mmsi) {
        return Optional.empty();
    }

    @Override
    public Vessel save(Vessel vessel) {
        return null;
    }

    @Override
    @Transactional
    public void processingVessel(VesselInfo vesselInfo) {
        var mmsi = vesselInfo.getMmsi();
        if (isAton(mmsi)) {
            return;
        }
        Optional<Vessel> byId = vesselRepository.findById(vesselInfo.getImo());
        if (byId.isEmpty()) {
            Vessel vessel = createNewVessel(vesselInfo);
            vesselRepository.save(vessel);
            return;
        }
        Vessel vessel = byId.get();
        updateVessel(vessel, vesselInfo);
    }

    private void updateVessel(Vessel vessel, VesselInfo vesselInfo) {
        var packageTimeVessel = vessel.getPackageTime().getTime();
        var packageTimeVesselUpdate = Timestamp.from(vesselInfo.getPackageTime()).getTime();
        if (packageTimeVessel < packageTimeVesselUpdate) {
            vesselMapper.updateVessel(vessel, vesselInfo);
        }
    }

    private Vessel createNewVessel(VesselInfo vesselInfo) {
        return vesselMapper.vesselKafkaToVessel(vesselInfo);
    }

    private boolean isAton(Long mmsi) {
        var mmsiValue = String.valueOf(mmsi);
        return mmsiValue.startsWith("999");
    }


}
