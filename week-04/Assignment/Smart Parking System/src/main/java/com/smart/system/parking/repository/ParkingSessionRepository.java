package com.smart.system.parking.repository;

import com.smart.system.parking.entity.ParkingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    @Query("SELECT p FROM ParkingSession p " +
            "JOIN FETCH p.vehicle " +
            "JOIN FETCH p.parkingSpot " +
            "WHERE p.vehicle.vehicleNumber = :vehicleNumber " +
            "AND p.exitTime IS NULL")
    Optional<ParkingSession> findActiveSessionByLicense(String vehicleNumber);
}

