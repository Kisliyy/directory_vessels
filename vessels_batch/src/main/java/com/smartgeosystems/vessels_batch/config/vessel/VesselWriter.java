package com.smartgeosystems.vessels_batch.config.vessel;

import com.smartgeosystems.vessels_batch.dto.VesselDbDto;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class VesselWriter {

    private final static String SQL_INSERT = "INSERT INTO vessels (imo, mmsi, mid, vessel_name, call_sign, ship_type_id, destination, eta, dimension_to_bow_a, " +
            "dimension_to_stern_b, dimension_to_port_c, dimension_to_starboard_d, draught, package_time, gen_width, gen_length, creation_time, update_time, deleted)" +
            "VALUES (:imo, :mmsi, :mid, :vesselName, :callSign, :shipTypeId, :destination, :eta, :dimensionToBowA, " +
            ":dimensionToSternB, :dimensionToPortC, :dimensionToStarboardD, :draught, :packageTime, :genWidth, :genLength, :creationTime, :updateTime, :deleted)";


    @Bean
    public JdbcBatchItemWriter<VesselDbDto> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<VesselDbDto>()
                .dataSource(dataSource)
                .sql(SQL_INSERT)
                .beanMapped()
                .build();
    }

}
