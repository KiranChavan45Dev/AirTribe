package com.smart.system.parking.service;

import com.smart.system.parking.dto.EntryRequest;
import com.smart.system.parking.dto.EntryResponse;
import com.smart.system.parking.dto.ExitRequest;
import com.smart.system.parking.dto.ExitResponse;
import com.smart.system.parking.enums.VehicleType;

public interface ParkingService {
    EntryResponse handleVehicleEntry(EntryRequest request);
    ExitResponse handleVehicleExit(ExitRequest request);
    int getAvailableSpotCount();
    int getAvailableSpotCount(VehicleType vehicleType);
}
