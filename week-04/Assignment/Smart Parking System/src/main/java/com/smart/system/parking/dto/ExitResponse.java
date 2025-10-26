package com.smart.system.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExitResponse {
    private String vehicleNumber;
    private double fee;
    private String entryTime;
    private String exitTime;

    public ExitResponse(String vehicleNumber, double fee, String exitTime) {
        this.vehicleNumber=vehicleNumber;
        this.fee=fee;
        this.exitTime=exitTime;
    }
}
