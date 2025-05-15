package com.example.frattab.dto;

import lombok.Data;

@Data
public class DrinkDto {
    private Long id;
    private String name;
    private Integer qty;
    private double price;
}
