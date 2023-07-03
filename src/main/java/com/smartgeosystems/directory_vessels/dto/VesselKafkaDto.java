package com.smartgeosystems.directory_vessels.dto;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VesselKafkaDto {
    private Long mmsi;
    private String vesselName;
    private String callSign;
    private Long imo;
    private Long shipTypeId;
    private String destination;
    private Instant eta;
    private Double dimensionToBowA;
    private Double dimensionToSternB;
    private Double dimensionToPortC;
    private Double dimensionToStarboardD;
    private Double draught;
    private Instant packageTime;

}
