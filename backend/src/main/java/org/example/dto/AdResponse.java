package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.enums.AdStatus;
import org.example.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AdResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Currency currency;
    private AdStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String ownerEmail;
}