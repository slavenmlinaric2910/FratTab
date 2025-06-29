package com.example.frattab.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BillingRunDetailDto {

    private List<DrinkLogDto> drinkLogs;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;

    private boolean isInvoiced;
    private boolean isPaid;
    private double total;
}
