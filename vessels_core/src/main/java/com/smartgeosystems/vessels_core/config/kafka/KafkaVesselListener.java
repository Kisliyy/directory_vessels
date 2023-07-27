package com.smartgeosystems.vessels_core.config.kafka;

import com.smartgeosystems.vessels_core.services.proccess.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.vmts.vessel.VesselInfo;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaVesselListener {
    private final ProcessService processService;

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics}",
            id = "${spring.kafka.consumer.client}",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "${spring.kafka.consumer.concurrency}"
    )
    public void listen(VesselInfo vesselInfo) {
        if (vesselInfo == null) {
            log.warn("Consume message is null!");
            return;
        }
        log.debug(vesselInfo.toString());
        processService.process(vesselInfo);
    }

}
