package com.example.frattab.services;

import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkSelectionDto;
import com.example.frattab.dto.QuickConsumptionDto;
import com.example.frattab.dto.ResponseDto;

public interface DrinkLogService {
    void logDrinks(DrinkLogDto dto);

    ResponseDto quickDrinkLog(QuickConsumptionDto dto);

    DrinkSelectionDto prepareSelection(Long memberId);

}
