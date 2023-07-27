package com.smartgeosystems.vessels_batch.config;

import com.smartgeosystems.vessels_batch.dto.VesselCsvDto;
import com.smartgeosystems.vessels_batch.dto.VesselDbDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class VesselBatch {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job importCsvToDb(Step transformVesselStep) {
        return new JobBuilder("importFromCsvToDbJob")
                .repository(jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(transformVesselStep)
                .build();
    }

    @Bean
    public Step transformVesselStep(ItemReader<VesselCsvDto> itemReader,
                                    ItemProcessor<VesselCsvDto, VesselDbDto> itemProcessor,
                                    ItemWriter<VesselDbDto> itemWriter) {
        return new StepBuilder("transformVesselStep")
                .repository(jobRepository)
                .transactionManager(platformTransactionManager)
                .<VesselCsvDto, VesselDbDto>chunk(15)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }


}
