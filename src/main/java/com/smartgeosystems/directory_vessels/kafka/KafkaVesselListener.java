package com.smartgeosystems.directory_vessels.kafka;

import com.smartgeosystems.directory_vessels.dto.VesselKafkaDto;
import com.smartgeosystems.directory_vessels.services.vessel.VesselService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaVesselListener {
    private final VesselService vesselService;

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics}",
            id = "${spring.kafka.consumer.client}",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "${spring.kafka.consumer.concurrency}"
    )
    public void listen(VesselKafkaDto vesselKafkaDto) {
        if (vesselKafkaDto == null) {
            log.warn("Consume message is null!");
            return;
        }
        vesselService.processingVessel(vesselKafkaDto);
    }

}
