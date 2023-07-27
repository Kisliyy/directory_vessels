package com.smartgeosystems.vessels_core.dto.vessels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class VesselUpdateDto {

    @JsonProperty(value = "imo")
    @NotNull(message = "Imo can't be null!")
    @Min(value = 0, message = "Imo can't have a negative value")
    private Long imo;

    @JsonProperty(value = "mmsi")
    @NotNull(message = "Mmsi can't be null!")
    @Min(value = 0, message = "Mmsi can't have a negative value")
    private Long mmsi;

    @JsonProperty(value = "vesselName")
    private String vesselName;

    @JsonProperty(value = "callSign")
    private String callSign;

    @JsonProperty(value = "shipTypeId")
    private Long shipTypeId;

    @JsonProperty(value = "destination")
    private String destination;

    @JsonProperty(value = "eta")
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
}
