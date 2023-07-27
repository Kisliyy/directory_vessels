package com.smartgeosystems.vessels_core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "vessels")
@Builder
public class Vessel {

    @Id
    @Column(name = "imo")
    private Long imo;

    @Column(name = "mmsi")
    private Long mmsi;

    @Column(name = "mid")
    private Long mid;

    @Column(name = "vessel_name")
    private String vesselName;

    @Column(name = "call_sign")
    private String callSign;

    @Column(name = "ship_type_id")
    private Long shipTypeId;

    @Column(name = "destination")
    private String destination;

    @Column(name = "eta")
    private Timestamp eta;

    @Column(name = "dimension_to_bow_a")
    private Double dimensionToBowA;

    @Column(name = "dimension_to_stern_b")
    private Double dimensionToSternB;

    @Column(name = "dimension_to_port_c")
    private Double dimensionToPortC;

    @Column(name = "dimension_to_starboard_d")
    private Double dimensionToStarboardD;

    @Column(name = "draught")
    private Double draught;

    @Column(name = "package_time")
    private Timestamp packageTime;

    @Column(name = "gen_length")
    private Double genLength;

    @Column(name = "gen_width")
    private Double genWidth;

    @CreationTimestamp
    @Column(name = "creation_time")
    private Timestamp creationTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private Timestamp updateTime;

    @Column(name = "deleted")
    private boolean deleted;

    @PreUpdate
    void update() {
        if (this.creationTime == null) {
            Date date = new Date();
            this.creationTime = new Timestamp(date.getTime());
        }
    }
}
