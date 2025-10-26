package com.smart.system.parking.service.impl;

import com.smart.system.parking.entity.ParkingSpot;
import com.smart.system.parking.enums.SpotStatus;
import com.smart.system.parking.enums.VehicleType;
import com.smart.system.parking.repository.ParkingSpotRepository;
import com.smart.system.parking.service.ParkingSpotService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingSpotServiceImpl implements ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotServiceImpl(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Override
    public List<ParkingSpot> getAllSpots(VehicleType vehicleType, SpotStatus status) {
        List<ParkingSpot> spots = parkingSpotRepository.findAll();

        // Apply filters dynamically (simple and flexible)
        return spots.stream()
                .filter(spot -> vehicleType == null || spot.getVehicleType() == vehicleType)
                .filter(spot -> status == null || spot.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ParkingSpot> getSpotById(Long id) {
        return parkingSpotRepository.findById(id);
    }

    @Override
    public ParkingSpot createSpot(ParkingSpot spot) {
        return parkingSpotRepository.save(spot);
    }

    @Override
    public ParkingSpot updateSpot(Long id, ParkingSpot updatedSpot) {
        return parkingSpotRepository.findById(id)
                .map(existing -> {
                    existing.setCode(updatedSpot.getCode());
                    existing.setVehicleType(updatedSpot.getVehicleType());
                    existing.setLevel(updatedSpot.getLevel());

                    // Sync occupied boolean and status
                    existing.setOccupied(updatedSpot.isOccupied());
                    existing.setStatus(updatedSpot.isOccupied() ? SpotStatus.OCCUPIED : SpotStatus.AVAILABLE);

                    return parkingSpotRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Parking spot not found with ID: " + id));
    }

    @Override
    public void deleteSpot(Long id) {
        parkingSpotRepository.deleteById(id);
    }

    @Override
    public Optional<ParkingSpot> findFirstAvailableSpot(VehicleType vehicleType) {
        return parkingSpotRepository.findFirstByVehicleTypeAndOccupiedFalse(vehicleType);
    }

    @Override
    public int countAvailableSpots() {
        return parkingSpotRepository.countAvailableSpots();
    }

    @Override
    public int countAvailableSpotsByVehicleType(VehicleType vehicleType) {
        return parkingSpotRepository.countAvailableSpotsByVehicleType(vehicleType);
    }
}
