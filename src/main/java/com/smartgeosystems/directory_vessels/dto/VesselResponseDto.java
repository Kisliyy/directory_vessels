package com.smartgeosystems.directory_vessels.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VesselResponseDto {

    @JsonProperty(value = "imo")
    private Long imo;

    @JsonProperty(value = "mmsi")
    private Long mmsi;

    @JsonProperty(value = "mid")
    private Long mid;

    @JsonProperty(value = "vesselName")
    private String vesselName;

    @JsonProperty(value = "callSign")
    private String callSign;

    @JsonProperty(value = "shipTypeId")
    private Long shipTypeId;

    @JsonProperty(value = "destination")
    private String destination;

    @JsonProperty(value = "eta")
    @DateTimeFormat(pattern = "dd.MM.yyyy T HH:mm:ss")
    private Timestamp eta;

    @JsonProperty(value = "dimensionToBowA")
    private Double dimensionToBowA;

    @JsonProperty(value = "dimensionToSternB")
    private Double dimensionToSternB;

    @JsonProperty(value = "dimensionToPortC")
    private Double dimensionToPortC;

    @JsonProperty(value = "dimensionToStarboardD")
    private Double dimensionToStarboardD;

    @JsonProperty(value = "draught")
    private Double draught;

    @JsonProperty(value = "packageTime")
    private Timestamp packageTime;

    @JsonProperty(value = "genLength")
    private Double genLength;

    @JsonProperty(value = "genWidth")
    private Double genWidth;
}
