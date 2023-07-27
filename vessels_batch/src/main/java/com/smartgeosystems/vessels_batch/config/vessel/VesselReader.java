package com.smartgeosystems.vessels_batch.config.vessel;

import com.smartgeosystems.vessels_batch.dto.VesselCsvDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;

@Configuration
public class VesselReader {

    @Bean
    @StepScope
    public FlatFileItemReader<VesselCsvDto> readerCsvFile(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
        return new FlatFileItemReaderBuilder<VesselCsvDto>()
                .name("vesselItemReader")
                .resource(new FileSystemResource(pathToFile))
                .delimited()
                .delimiter(",")
                .names("imo", "mmsi", "vesselName", "callSign", "shipTypeId", "destination", "eta", "dimensionToBowA", "dimensionToSternB", "dimensionToPortC", "dimensionToStarboardD", "draught", "packageTime")
                .fieldSetMapper(getBeanWrapperFieldSetMapper())
                .fieldSetMapper(new VesselFieldSetMapper())
                .build();
    }

    private BeanWrapperFieldSetMapper<VesselCsvDto> getBeanWrapperFieldSetMapper() {
        BeanWrapperFieldSetMapper<VesselCsvDto> vesselDtoBeanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        vesselDtoBeanWrapperFieldSetMapper.setTargetType(VesselCsvDto.class);
        return vesselDtoBeanWrapperFieldSetMapper;
    }


    private static class VesselFieldSetMapper implements FieldSetMapper<VesselCsvDto> {

        @Override
        public @NonNull VesselCsvDto mapFieldSet(FieldSet fieldSet) {
            return VesselCsvDto
                    .builder()
                    .imo(fieldSet.readLong("imo"))
                    .mmsi(fieldSet.readLong("mmsi"))
                    .vesselName(fieldSet.readString("vesselName"))
                    .callSign(fieldSet.readString("callSign"))
                    .shipTypeId(fieldSet.readLong("shipTypeId"))
                    .destination(fieldSet.readString("destination"))
                    .eta(getTimestamp(fieldSet.readLong("eta")))
                    .dimensionToBowA(fieldSet.readDouble("dimensionToBowA"))
                    .dimensionToSternB(fieldSet.readDouble("dimensionToSternB"))
                    .dimensionToPortC(fieldSet.readDouble("dimensionToPortC"))
                    .dimensionToStarboardD(fieldSet.readDouble("dimensionToStarboardD"))
                    .draught(fieldSet.readDouble("draught"))
                    .packageTime(getTimestamp(fieldSet.readLong("packageTime")))
                    .build();
        }

        private Timestamp getTimestamp(Long value) {
            return value != null ? new Timestamp(value) : null;
        }
    }
}
