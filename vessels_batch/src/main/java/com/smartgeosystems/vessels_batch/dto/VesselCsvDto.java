package com.smartgeosystems.vessels_batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VesselCsvDto {

    private Long imo;
    private Long mmsi;
    private String vesselName;
    private String callSign;
    private Long shipTypeId;
    private String destination;
    private Timestamp eta;
    private Double dimensionToBowA;
    private Double dimensionToSternB;
    private Double dimensionToPortC;
    private Double dimensionToStarboardD;
    private Double draught;
    private Timestamp packageTime;
}
