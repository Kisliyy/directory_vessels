package com.smartgeosystems.directory_vessels.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;
import org.vmts.vessel.VesselInfo;


@Slf4j
public class VesselDeserializer implements Deserializer<VesselInfo> {
    @Override
    public VesselInfo deserialize(String topic, byte[] data) {
        if (data == null) {
            log.warn("Null received at deserializing");
            return null;
        }
        try {
            DatumReader<GenericRecord> datumReader =
                    new SpecificDatumReader<>(VesselInfo.class.getDeclaredConstructor().newInstance().getSchema());
            Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
            return (VesselInfo) datumReader.read(null, decoder);
        } catch (Exception e) {
            throw new RuntimeException("Error when deserializing byte[] to object", e);
        }
    }
}
