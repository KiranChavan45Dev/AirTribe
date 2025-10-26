package com.smart.system.parking.entity;

import com.smart.system.parking.enums.SpotStatus;
import com.smart.system.parking.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_spot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;


    private String code; // Spot identifier (e.g., A1, B2)

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    private SpotStatus status;

    private int level;

    private boolean occupied;
}
