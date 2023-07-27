package com.smartgeosystems.vessels_batch.config.vessel;

import com.smartgeosystems.vessels_batch.dto.VesselCsvDto;
import com.smartgeosystems.vessels_batch.dto.VesselDbDto;
import com.smartgeosystems.vessels_batch.mapper.BatchMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Configuration
public class VesselCustomItemProcessor {

    @Bean
    public ItemProcessor<VesselCsvDto, VesselDbDto> vesselProcessor() {
        return new VesselItemProcessor();
    }

    private static class VesselItemProcessor implements ItemProcessor<VesselCsvDto, VesselDbDto> {

        private final BatchMapper batchMapper = Mappers.getMapper(BatchMapper.class);

        @Override
        public VesselDbDto process(@NonNull VesselCsvDto item) {
            return batchMapper.mapCsv(item);
        }
    }
}
