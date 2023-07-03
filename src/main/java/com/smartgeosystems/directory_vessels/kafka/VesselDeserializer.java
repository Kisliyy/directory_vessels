package com.smartgeosystems.directory_vessels.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartgeosystems.directory_vessels.dto.VesselKafkaDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

@Slf4j
public class VesselDeserializer implements Deserializer<VesselKafkaDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public VesselKafkaDto deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.warn("Null received at deserializing");
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), VesselKafkaDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to VesselKafkaDto");
        }
    }
}
