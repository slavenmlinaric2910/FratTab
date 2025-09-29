package com.example.frattab.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frattab.dto.DeliveryRequestDto;
import com.example.frattab.dto.DrinkDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Delivery;
import com.example.frattab.models.Drink;
import com.example.frattab.services.DrinksService;
import com.example.frattab.util.Mappers;
import com.example.frattab.repositories.DeliveryRepository;
import com.example.frattab.repositories.DrinkRepository;

@Service
public class DrinksServiceImpl implements DrinksService {
    @Autowired
    private DrinkRepository drinkRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private Mappers mappers;

    @Override
    public List<Drink> getAllDrinks() {
        return drinkRepository.findAll();
    }

    @Override
    public List<Drink> getAllActiveDrinks() {
        return drinkRepository.findAllByIsActiveTrue();
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
    public ResponseDto deactivateDrink(Long drinkId) {
        ResponseDto responseDto = new ResponseDto();
        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new RuntimeException("Drink not found"));
        drink.setActive(false);
        drinkRepository.save(drink);
        responseDto.setStatus("success");
        responseDto.setMessage("Drink " + drinkId + " has been deactivated successfully");
        return responseDto;
    }

    @Override
    public ResponseDto addDrinkDelivery(DeliveryRequestDto deliveryDto) {
        ResponseDto responseDto = new ResponseDto();
        Drink drink = drinkRepository.findById(deliveryDto.getDrinkId())
                .orElseThrow(() -> new RuntimeException("Drink not found"));
        int updateQty = drink.getQty() + deliveryDto.getQty();
        drink.setQty(updateQty);
        drinkRepository.save(drink);
        Delivery delivery = new Delivery();
        delivery.setDrink(drink);
        delivery.setQty(deliveryDto.getQty());
        delivery.setTotal(deliveryDto.getPrice() * deliveryDto.getQty());
        deliveryRepository.save(delivery);
        drink.setPrice(BigDecimal.valueOf(deliveryDto.getPrice() * 0.2 + deliveryDto
                .getPrice())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());
        drinkRepository.save(drink);
        responseDto.setStatus("success");
        responseDto.setMessage("Delivery of " + deliveryDto.getQty() + " " + drink.getName() + "(s) has been added");
        return responseDto;
    }

    @Override
    public Double getMonthlyDrinkSpending() {
        System.out.println("sum" + deliveryRepository.sumTotalForCurrentMonth());
        return deliveryRepository.sumTotalForCurrentMonth();
    }

    @Override
    public void updateDrinkQty(Long drinkId, int qty) {
        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new RuntimeException("Drink not found"));
        int newQty = drink.getQty() - qty;
        drink.setQty(newQty);
        drinkRepository.save(drink);
    }

}
