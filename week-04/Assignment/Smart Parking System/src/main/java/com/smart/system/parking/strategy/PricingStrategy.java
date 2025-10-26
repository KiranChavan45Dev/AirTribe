package com.smart.system.parking.strategy;

import com.smart.system.parking.entity.ParkingSession;

public interface PricingStrategy {
    double calculateFee(ParkingSession session);
}
