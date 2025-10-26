package com.smart.system.parking.strategy;

import com.smart.system.parking.entity.ParkingSpot;
import com.smart.system.parking.enums.VehicleType;

import java.util.Optional;

public interface AllocationStrategy {
    Optional<ParkingSpot> allocate(VehicleType vehicleType);
}
