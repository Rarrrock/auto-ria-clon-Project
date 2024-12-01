package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarResponse {
    private Long id;
    private String model;
    private Integer enginePower;
    private Integer torque;
    private String ownerEmail;
    private Long lastMaintenanceTimestamp;
}