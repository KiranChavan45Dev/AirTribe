package com.smart.system.parking.strategy.impl;

import com.smart.system.parking.entity.ParkingSession;
import com.smart.system.parking.enums.VehicleType;
import com.smart.system.parking.strategy.PricingStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.smart.system.parking.enums.VehicleType.BUS;
import static com.smart.system.parking.enums.VehicleType.CAR;

@Component
@Qualifier("HourlyPricingStrategy")
public class HourlyPricingStrategy implements PricingStrategy {
    @Override
    public double calculateFee(ParkingSession session) {
        if (session.getEntryTime() == null || session.getExitTime() == null) {
            throw new IllegalArgumentException("Session must have entry and exit times set.");
        }

        long minutes = Duration.between(session.getEntryTime(), session.getExitTime()).toMinutes();
        long hours = Math.max(1, (minutes + 59) / 60); // Round up to next hour

        double rate;
        VehicleType type = session.getVehicle().getVehicleType();
        switch (type) {
            case BIKE -> rate = 5;
            case BUS -> rate = 20;
            default -> rate = 10;
        }
        return rate * hours;
    }
}
