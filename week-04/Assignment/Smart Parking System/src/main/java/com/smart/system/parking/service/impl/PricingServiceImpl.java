package com.smart.system.parking.service.impl;

import com.smart.system.parking.entity.ParkingSession;
import com.smart.system.parking.enums.VehicleType;
import com.smart.system.parking.service.PricingService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PricingServiceImpl implements PricingService {
    @Override
    public Double calculateFee(ParkingSession session) {
        if (session.getExitTime() == null || session.getEntryTime() == null) {
            throw new IllegalArgumentException("Entry and exit time must be set.");
        }

        long minutes = Duration.between(session.getEntryTime(), session.getExitTime()).toMinutes();
        long hours = Math.max(1, minutes / 60);

        VehicleType type = session.getVehicle().getVehicleType();
        double ratePerHour;

        switch (type) {
            case BIKE -> ratePerHour = 5;
            case BUS -> ratePerHour = 20;
            default -> ratePerHour = 10;
        }

        return ratePerHour * hours;
    }
}
