// src/main/java/com/example/frattab/dto/MemberBillDto.java
package com.example.frattab.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberBillDto {
    private Long memberId;
    private String firstName;
    private String lastName;
    private String email;

    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;  // exclusive

    private Double amountOwed;        // sum of DrinkLog.total in [periodStart, periodEnd)
}
