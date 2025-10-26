package com.smart.system.parking.controller;

import com.smart.system.parking.entity.ParkingSpot;
import com.smart.system.parking.enums.SpotStatus;
import com.smart.system.parking.enums.VehicleType;
import com.smart.system.parking.service.ParkingSpotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * REST controller for managing parking spots.
 */
@RestController
@RequestMapping("/api/spots")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    /**
     * Returns all parking spots, optionally filtered by vehicle type or status.
     */
    @GetMapping
    public ResponseEntity<List<ParkingSpot>> getAllSpots(
            @RequestParam(required = false) VehicleType vehicleType,
            @RequestParam(required = false) SpotStatus status
    ) {
        List<ParkingSpot> spots = parkingSpotService.getAllSpots(vehicleType, status);
        return ResponseEntity.ok(spots.isEmpty() ? Collections.emptyList() : spots);
    }

    /**
     * Returns a single parking spot by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpot> getSpotById(@PathVariable Long id) {
        return parkingSpotService.getSpotById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok((ParkingSpot) Collections.emptyMap())); // return {}
    }

    /**
     * Creates a new parking spot.
     */
    @PostMapping
    public ResponseEntity<ParkingSpot> createSpot(@RequestBody ParkingSpot spot) {
        ParkingSpot created = parkingSpotService.createSpot(spot);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Updates an existing parking spot.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpot> updateSpot(
            @PathVariable Long id,
            @RequestBody ParkingSpot spot
    ) {
        try {
            ParkingSpot updated = parkingSpotService.updateSpot(id, spot);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.ok((ParkingSpot) Collections.emptyMap()); // return {}
        }
    }

    /**
     * Deletes a parking spot by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpot(@PathVariable Long id) {
        parkingSpotService.deleteSpot(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Finds the first available parking spot for a specific vehicle type.
     */
    @GetMapping("/available/first")
    public ResponseEntity<ParkingSpot> getFirstAvailableSpot(
            @RequestParam VehicleType vehicleType
    ) {
        return parkingSpotService.findFirstAvailableSpot(vehicleType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok((ParkingSpot) Collections.emptyMap()));
    }

    /**
     * Returns total number of available parking spots.
     */
    @GetMapping("/available/count")
    public ResponseEntity<Integer> countAvailableSpots() {
        return ResponseEntity.ok(parkingSpotService.countAvailableSpots());
    }

    /**
     * Returns number of available parking spots by vehicle type.
     */
    @GetMapping("/available/count-by-type")
    public ResponseEntity<Integer> countAvailableSpotsByVehicleType(
            @RequestParam VehicleType vehicleType
    ) {
        return ResponseEntity.ok(parkingSpotService.countAvailableSpotsByVehicleType(vehicleType));
    }
}
