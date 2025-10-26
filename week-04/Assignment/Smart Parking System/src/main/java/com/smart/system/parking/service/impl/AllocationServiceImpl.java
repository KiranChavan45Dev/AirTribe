package com.smart.system.parking.service.impl;

import com.smart.system.parking.entity.ParkingSpot;
import com.smart.system.parking.enums.VehicleType;
import com.smart.system.parking.exception.InvalidSessionException;
import com.smart.system.parking.repository.ParkingSpotRepository;
import com.smart.system.parking.service.AllocationService;
import com.smart.system.parking.strategy.AllocationStrategy;
import com.smart.system.parking.strategy.StrategyContext;
import com.smart.system.parking.util.LockManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.locks.Lock;

@Service
public class AllocationServiceImpl implements AllocationService {

    @Autowired
    private ParkingSpotRepository spotRepository;

    @Autowired
    private LockManager lockManager;

    @Autowired
    private StrategyContext strategyContext; // Holds allocation strategies

    /**
     * Allocate a spot using the given allocation strategy
     * @param vehicleType Type of vehicle
     * @param strategyName Name of allocation strategy to use
     */
    public ParkingSpot allocateSpot(VehicleType vehicleType, String strategyName) {
        Lock lock = lockManager.getLock(vehicleType.name());
        lock.lock();
        try {
            AllocationStrategy strategy = strategyContext.getAllocationStrategy();
            Optional<ParkingSpot> optionalSpot = strategy.allocate(vehicleType);

            if (optionalSpot.isEmpty()) {
                throw new InvalidSessionException("No available spot for type: " + vehicleType);
            }

            ParkingSpot spot = optionalSpot.get();
            spot.setOccupied(true);
            spotRepository.save(spot);
            return spot;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void releaseSpot(Long spotId) {
        Optional<ParkingSpot> optionalSpot = spotRepository.findById(spotId);
        if (optionalSpot.isEmpty()) return;
        ParkingSpot spot = optionalSpot.get();
        spot.setOccupied(false);
        spotRepository.save(spot);
    }

    /**
     * Deprecated default allocation for backward compatibility
     */
    @Override
    public ParkingSpot allocateSpot(VehicleType vehicleType) {
        return allocateSpot(vehicleType, "nearestFirst"); // default strategy
    }
}
