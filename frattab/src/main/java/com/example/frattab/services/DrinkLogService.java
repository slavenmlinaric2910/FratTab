package com.example.frattab.services;

import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkSelectionDto;

public interface DrinkLogService {
    void logDrinks(DrinkLogDto dto);
      DrinkSelectionDto prepareSelection(Long memberId);

}
