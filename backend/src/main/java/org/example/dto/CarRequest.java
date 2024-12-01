package org.example.dto;

import lombok.Data;

@Data
public class CarRequest {
    private String model;
    private Integer enginePower;
    private Integer torque;
    private Long ownerId;
    private Long lastMaintenanceTimestamp;
}