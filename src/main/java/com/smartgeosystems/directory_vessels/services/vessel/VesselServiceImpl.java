package com.smartgeosystems.directory_vessels.services.vessel;

import com.smartgeosystems.directory_vessels.dto.VesselKafkaDto;
import com.smartgeosystems.directory_vessels.mappers.VesselMapper;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.repository.VesselRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void processingVessel(VesselKafkaDto vesselKafkaDto) {
        var mmsi = vesselKafkaDto.getMmsi();
        Optional<Vessel> byMmsi = vesselRepository.findByMmsi(mmsi);
        if (byMmsi.isEmpty()) {
            Vessel vessel = createNewVessel(vesselKafkaDto);
            return;
        }
        Vessel vessel = byMmsi.get();
        updateVessel(vessel, vesselKafkaDto);
    }

    private void updateVessel(Vessel vessel, VesselKafkaDto vesselKafkaDto) {

    }

    private Vessel createNewVessel(VesselKafkaDto vesselKafkaDto) {

        return vesselMapper.vesselKafkaToVessel(vesselKafkaDto);
    }
}
