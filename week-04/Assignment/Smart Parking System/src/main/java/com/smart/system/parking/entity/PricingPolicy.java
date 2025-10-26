package com.smart.system.parking.entity;

import com.smart.system.parking.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="pricing_policy")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private Double ratePerHour;
}
