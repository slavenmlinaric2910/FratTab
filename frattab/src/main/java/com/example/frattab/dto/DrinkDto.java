package com.example.frattab.dto;

import lombok.Data;

@Data
public class DrinkDto {
    private Long id;
    private String name;
    private int qty;
    private double price;
}
