package com.smart.system.parking.service;

import com.smart.system.parking.entity.ParkingSpot;
import com.smart.system.parking.enums.SpotStatus;
import com.smart.system.parking.enums.VehicleType;

import java.util.List;
import java.util.Optional;

public interface ParkingSpotService {

    List<ParkingSpot> getAllSpots(VehicleType vehicleType, SpotStatus status);

    Optional<ParkingSpot> getSpotById(Long id);

    ParkingSpot createSpot(ParkingSpot spot);

    ParkingSpot updateSpot(Long id, ParkingSpot updatedSpot);

    void deleteSpot(Long id);

    Optional<ParkingSpot> findFirstAvailableSpot(VehicleType vehicleType);

    int countAvailableSpots();

    int countAvailableSpotsByVehicleType(VehicleType vehicleType);
}
