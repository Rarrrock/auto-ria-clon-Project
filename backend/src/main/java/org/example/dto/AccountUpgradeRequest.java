package org.example.dto;

import lombok.Data;

@Data
public class AccountUpgradeRequest {
    private Long userId;
    private String requestedType; // "PREMIUM"
}