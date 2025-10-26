package com.smart.system.parking.strategy.impl;

import com.smart.system.parking.entity.ParkingSpot;
import com.smart.system.parking.enums.VehicleType;
import com.smart.system.parking.repository.ParkingSpotRepository;
import com.smart.system.parking.strategy.AllocationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Optional;

@Component
@Qualifier("NearestFirst")
public class NearestFirst implements AllocationStrategy {

    @Autowired
    private ParkingSpotRepository spotRepository;

    @Override
    public Optional<ParkingSpot> allocate(VehicleType vehicleType) {
        return spotRepository.findAll().stream()
                .filter(spot -> spot.getVehicleType() == vehicleType && !spot.isOccupied())
                .min(Comparator.comparingInt(ParkingSpot::getLevel));
    }
}
