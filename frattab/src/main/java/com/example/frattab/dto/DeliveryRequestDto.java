package com.example.frattab.dto;

import lombok.Data;

@Data
public class DeliveryRequestDto {
    private Long drinkId;
    private double price;
    private int qty;
}
