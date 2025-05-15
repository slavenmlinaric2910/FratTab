package com.example.frattab.dto;

import lombok.Data;

@Data
public class QuickConsumptionDto {
    private Long memberId;
    private Long drinkId;
    private int quantity;
}
