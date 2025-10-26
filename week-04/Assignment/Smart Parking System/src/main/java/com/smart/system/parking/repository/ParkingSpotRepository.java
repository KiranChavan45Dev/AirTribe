package com.smart.system.parking.repository;

import com.smart.system.parking.entity.ParkingSpot;
import com.smart.system.parking.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot,Long> {
    Optional<ParkingSpot> findFirstByVehicleTypeAndOccupiedFalse(VehicleType type);

    @Query("SELECT COUNT(p) FROM ParkingSpot p WHERE p.occupied = false")
    int countAvailableSpots();

    @Query("SELECT COUNT(p) FROM ParkingSpot p WHERE p.occupied = false AND p.vehicleType = :vehicleType")
    int countAvailableSpotsByVehicleType(VehicleType vehicleType);
}
