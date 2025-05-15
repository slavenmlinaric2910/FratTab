// src/main/java/com/example/frattab/services/DrinkLogService.java
package com.example.frattab.services;

import java.util.List;

import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Drink;

public interface DrinkLogService {
    ResponseDto logDrink(DrinkLogDto drinkLogDto);

    DrinkLogDto setDrinkLogDto(List<Drink> drinks, Long memberId);
}
