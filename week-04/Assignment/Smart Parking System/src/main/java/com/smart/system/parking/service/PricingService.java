package com.smart.system.parking.service;

import com.smart.system.parking.entity.ParkingSession;

public interface PricingService {
    Double calculateFee(ParkingSession session);
}
