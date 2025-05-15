package com.example.frattab.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frattab.dto.DeliveryDto;
import com.example.frattab.dto.DrinkDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Drink;
import com.example.frattab.services.DrinksService;
import com.example.frattab.util.Mappers;
import com.example.frattab.repositories.DrinkRepository;

@Service
public class DrinksServiceImpl implements DrinksService {
    @Autowired
    private DrinkRepository drinkRepository;
    @Autowired
    private Mappers mappers;

    @Override
    public List<Drink> getAllDrinks() {
        return drinkRepository.findAll();
    }

    @Override
    public ResponseDto addNewDrink(DrinkDto drinkDto) {
        ResponseDto responseDto = new ResponseDto();
        drinkRepository.save(mappers.drinkDtoToDrink(drinkDto));
        responseDto.setStatus("success");
        responseDto.setMessage("Drink " + drinkDto.getName() + " has been added successfully");
        return responseDto;
    }

    @Override
    public ResponseDto addDrinkDelivery(DeliveryDto deliveryDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addDrinkDelivery'");
    }

}
