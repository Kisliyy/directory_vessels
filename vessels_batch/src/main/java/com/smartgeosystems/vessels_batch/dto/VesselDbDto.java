package com.smartgeosystems.vessels_batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VesselDbDto {

    private Long imo;
    private Long mmsi;
    private Long mid;
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
    private Double genLength;
    private Double genWidth;
    private Timestamp creationTime;
    private Timestamp updateTime;
    private boolean deleted;
}
