package com.example.frattab.dto;

import java.util.List;

import com.example.frattab.models.Drink;
import com.example.frattab.models.Member;

import lombok.Data;

@Data
public class DrinkSelectionDto {
  private Member member;
  private List<Drink> drinks;
  private DrinkLogDto drinkLogDto;
}
