package com.example.frattab.services;

import java.util.List;

import com.example.frattab.dto.DeliveryRequestDto;
import com.example.frattab.dto.DrinkDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Drink;

public interface DrinksService {
    List<Drink> getAllDrinks();

    List<Drink> getAllActiveDrinks();

    ResponseDto deactivateDrink(Long drinkId);

    ResponseDto addNewDrink(DrinkDto drinkDto);

    ResponseDto addDrinkDelivery(DeliveryRequestDto deliveryDto);

    Double getMonthlyDrinkSpending();

    void updateDrinkQty(Long drinkId, int qty);


}
