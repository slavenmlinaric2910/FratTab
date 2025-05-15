package com.example.frattab.services;

import org.springframework.data.domain.Page;

import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkSelectionDto;
import com.example.frattab.dto.QuickConsumptionDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.DrinkLog;

public interface DrinkLogService {
    void logDrinks(DrinkLogDto dto);

    ResponseDto quickDrinkLog(QuickConsumptionDto dto);

    DrinkSelectionDto prepareSelection(Long memberId);

    Page<DrinkLog> getRecentDrinkLogs(int page);

}
