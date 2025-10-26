package com.smart.system.parking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_session")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", columnDefinition = "BIGINT")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "spot_id", columnDefinition = "BIGINT")
    private ParkingSpot parkingSpot;


    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private double fee;

}
