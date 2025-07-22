package com.example.frattab.dto;

import lombok.Data;

@Data
public class DrinkQtyDto {
    private Long id;
    private Long drinkId;
    private DrinkDto drink;
    private int qty;
}
