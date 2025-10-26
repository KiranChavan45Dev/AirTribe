package com.smart.system.parking.controller;

import com.smart.system.parking.dto.EntryRequest;
import com.smart.system.parking.dto.EntryResponse;
import com.smart.system.parking.dto.ExitRequest;
import com.smart.system.parking.dto.ExitResponse;
import com.smart.system.parking.entity.ParkingSpot;
import com.smart.system.parking.enums.SpotStatus;
import com.smart.system.parking.enums.VehicleType;
import com.smart.system.parking.service.ParkingService;
import com.smart.system.parking.service.ParkingSpotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;
    private final ParkingSpotService parkingSpotService;

    public ParkingController(ParkingService parkingService, ParkingSpotService parkingSpotService) {
        this.parkingService = parkingService;
        this.parkingSpotService = parkingSpotService;
    }

    /**
     * Handles vehicle entry.
     * Strategy selection is now handled internally via StrategyContext.
     */
    @PostMapping("/entry")
    public ResponseEntity<EntryResponse> handleVehicleEntry(@RequestBody EntryRequest request) {
        EntryResponse response = parkingService.handleVehicleEntry(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Handles vehicle exit and calculates parking fee dynamically.
     * Pricing strategy selection is now handled internally via StrategyContext.
     */
    @PostMapping("/exit")
    public ResponseEntity<ExitResponse> handleVehicleExit(@RequestBody ExitRequest request) {
        ExitResponse response = parkingService.handleVehicleExit(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Returns available parking spots (optionally filtered by vehicle type).
     */
    @GetMapping("/availability")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpots(
            @RequestParam(required = false) VehicleType vehicleType) {

        List<ParkingSpot> spots = parkingSpotService.getAllSpots(vehicleType, SpotStatus.AVAILABLE);
        return ResponseEntity.ok(spots);
    }

    /**
     * Returns count of available parking spots (optionally filtered by vehicle type).
     */
    @GetMapping("/availability/count")
    public ResponseEntity<Integer> getAvailableSpotCount(
            @RequestParam(required = false) VehicleType vehicleType) {

        int count = (vehicleType == null)
                ? parkingService.getAvailableSpotCount()
                : parkingService.getAvailableSpotCount(vehicleType);

        return ResponseEntity.ok(count);
    }

    public static class GlobalException {
    }
}
