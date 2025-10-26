package com.smart.system.parking.dto;

import com.smart.system.parking.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryRequest {
    private String vehicleNumber;
    private VehicleType vehicleType;
}
