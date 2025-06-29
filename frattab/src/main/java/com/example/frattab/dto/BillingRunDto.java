package com.example.frattab.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BillingRunDto {
    private Long id;
    private LocalDateTime runTimestamp; // Use String for JSON serialization, format as needed
}
