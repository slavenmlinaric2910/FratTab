package com.example.frattab.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DrinkLogDto {
    private Long id;
    private Long memberId;
    private List<DrinkQtyDto> drinkQuantities = new ArrayList<>();
    private double total;
    private boolean isBilled;
    private boolean isPaid;
    private LocalDateTime createdAt;
    private BillingRunDto billingRun;
}
