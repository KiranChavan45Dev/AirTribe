package com.smart.system.parking.service.impl;

import com.smart.system.parking.dto.EntryRequest;
import com.smart.system.parking.dto.EntryResponse;
import com.smart.system.parking.dto.ExitRequest;
import com.smart.system.parking.dto.ExitResponse;
import com.smart.system.parking.entity.ParkingSession;
import com.smart.system.parking.entity.ParkingSpot;
import com.smart.system.parking.entity.Vehicle;
import com.smart.system.parking.enums.SpotStatus;
import com.smart.system.parking.enums.VehicleType;
import com.smart.system.parking.exception.InvalidSessionException;
import com.smart.system.parking.repository.ParkingSessionRepository;
import com.smart.system.parking.repository.ParkingSpotRepository;
import com.smart.system.parking.repository.VehicleRepository;
import com.smart.system.parking.service.AllocationService;
import com.smart.system.parking.service.ParkingService;
import com.smart.system.parking.service.PricingService;
import com.smart.system.parking.strategy.AllocationStrategy;
import com.smart.system.parking.strategy.PricingStrategy;
import com.smart.system.parking.strategy.StrategyContext;
import com.smart.system.parking.util.LockManager;
import com.smart.system.parking.util.TimeUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final ParkingSpotRepository spotRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSessionRepository sessionRepository;
    private final LockManager lockManager;
    private final StrategyContext strategyContext;

    public ParkingServiceImpl(ParkingSpotRepository spotRepository,
                              VehicleRepository vehicleRepository,
                              ParkingSessionRepository sessionRepository,
                              LockManager lockManager,
                              StrategyContext strategyContext) {
        this.spotRepository = spotRepository;
        this.vehicleRepository = vehicleRepository;
        this.sessionRepository = sessionRepository;
        this.lockManager = lockManager;
        this.strategyContext = strategyContext;
    }

    @Override
    @Transactional
    public EntryResponse handleVehicleEntry(EntryRequest request) {
        VehicleType type = request.getVehicleType();
        Lock lock = lockManager.getLock(type.name());
        lock.lock();

        try {
            AllocationStrategy allocationStrategy = strategyContext.getAllocationStrategy();

            ParkingSpot spot = allocationStrategy.allocate(type)
                    .orElseThrow(() -> new InvalidSessionException("No available spot for type: " + type));

            Vehicle vehicle = vehicleRepository.findByVehicleNumber(request.getVehicleNumber())
                    .orElseGet(() -> vehicleRepository.save(new Vehicle(request.getVehicleNumber(), type)));

            ParkingSession session = new ParkingSession();
            session.setVehicle(vehicle);
            session.setParkingSpot(spot);
            session.setEntryTime(LocalDateTime.now());
            sessionRepository.save(session);

            spot.setOccupied(true);
            spot.setStatus(SpotStatus.OCCUPIED); // update status when occupied
            spotRepository.save(spot);


            return new EntryResponse(vehicle.getVehicleNumber(), spot.getId(), TimeUtil.nowAsString());
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public ExitResponse handleVehicleExit(ExitRequest request) {
        // Fetch active session with vehicle and spot eagerly loaded
        ParkingSession session = sessionRepository
                .findActiveSessionByLicense(request.getVehicleNumber())
                .orElseThrow(() -> new InvalidSessionException(
                        "Active session not found for license: " + request.getVehicleNumber()
                ));

        // Set exit time now
        LocalDateTime exitTime = LocalDateTime.now();
        session.setExitTime(exitTime);

        // Calculate fee dynamically using pricing strategy
        PricingStrategy pricingStrategy = strategyContext.getPricingStrategy();
        double fee = pricingStrategy.calculateFee(session);
        session.setFee(fee);
        // Update session in DB
        sessionRepository.save(session);

        // Free the parking spot
        ParkingSpot spot = session.getParkingSpot();
        if (spot != null) {
            spot.setOccupied(false);
            spot.setStatus(SpotStatus.AVAILABLE); // update status when freed
            spotRepository.save(spot);
        }

        // Safely convert entry and exit times to string
        String entryTimeStr = session.getEntryTime() != null
                ? TimeUtil.toString(session.getEntryTime())
                : "N/A"; // fallback if somehow null

        String exitTimeStr = TimeUtil.toString(exitTime);

        return new ExitResponse(request.getVehicleNumber(), fee, entryTimeStr, exitTimeStr);

    }

    @Override
    public int getAvailableSpotCount() {
        return spotRepository.countAvailableSpots();
    }

    @Override
    public int getAvailableSpotCount(VehicleType vehicleType) {
        return spotRepository.countAvailableSpotsByVehicleType(vehicleType);
    }
}
