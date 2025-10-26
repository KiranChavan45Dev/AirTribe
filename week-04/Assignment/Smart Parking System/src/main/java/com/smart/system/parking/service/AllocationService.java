package com.smart.system.parking.service;

import com.smart.system.parking.entity.ParkingSpot;
import com.smart.system.parking.enums.VehicleType;

public interface AllocationService {
    ParkingSpot allocateSpot(VehicleType vehicleType);
    void releaseSpot(Long spotId);
}
