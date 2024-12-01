package org.example.dto;

import lombok.Data;
import org.example.enums.Currency;

import java.math.BigDecimal;

@Data
public class AdRequest {
    private String title; // Название объявления
    private String description; // Описание
    private BigDecimal price; // Цена (заменено на BigDecimal)
    private Currency currency; // Валюта
}