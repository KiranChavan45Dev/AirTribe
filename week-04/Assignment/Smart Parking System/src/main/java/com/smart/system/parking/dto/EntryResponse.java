package com.smart.system.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryResponse {
    private String vehicleNumber;
    private Long spotId;
    private String entryTime;
}
