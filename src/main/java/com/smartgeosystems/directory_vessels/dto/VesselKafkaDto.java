package com.smartgeosystems.directory_vessels.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VesselKafkaDto {
    @JsonProperty(value = "Imo")
    private Long imo;
    @JsonProperty(value = "Mmsi")
    private Long mmsi;
    @JsonProperty(value = "VesselName")
    private String vesselName;
    @JsonProperty(value = "CallSign")
    private String callSign;
    @JsonProperty(value = "ShipTypeId")
    private Long shipTypeId;
    @JsonProperty(value = "Destination")
    private String destination;
    @JsonProperty(value = "Eta")
    private Instant eta;
    @JsonProperty(value = "DimensionToBowA")
    private Double dimensionToBowA;
    @JsonProperty(value = "DimensionToSternB")
    private Double dimensionToSternB;
    @JsonProperty(value = "DimensionToPortC")
    private Double dimensionToPortC;
    @JsonProperty(value = "DimensionToStarboardD")
    private Double dimensionToStarboardD;
    @JsonProperty(value = "Draught")
    private Double draught;
    @JsonProperty(value = "PackageTime")
    private Instant packageTime;
}
