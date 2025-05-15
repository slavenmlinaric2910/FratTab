package com.example.frattab.services;

import java.util.List;

import com.example.frattab.dto.DeliveryRequestDto;
import com.example.frattab.dto.DrinkDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Drink;

public interface DrinksService {
    List<Drink> getAllDrinks();

    ResponseDto addNewDrink(DrinkDto drinkDto);

    ResponseDto addDrinkDelivery(DeliveryRequestDto deliveryDto);

    Double getMonthlyDrinkSpending();
}
