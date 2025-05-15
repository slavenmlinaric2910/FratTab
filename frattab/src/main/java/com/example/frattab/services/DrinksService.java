package com.example.frattab.services;

import com.example.frattab.dto.DeliveryDto;
import com.example.frattab.dto.DrinkDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Drink;
import java.util.List;

public interface DrinksService {
    List<Drink> getAllDrinks();

    ResponseDto addNewDrink(DrinkDto drinkDto);

    ResponseDto addDrinkDelivery(DeliveryDto deliveryDto);
}
