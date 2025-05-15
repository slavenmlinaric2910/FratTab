package com.example.frattab.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frattab.models.Drink;
import com.example.frattab.services.DrinksService;
import com.example.frattab.repositories.DrinkRepository;

@Service
public class DrinksServiceImpl implements DrinksService {
    @Autowired
    private DrinkRepository drinkRepository;

    @Override
    public List<Drink> getAllDrinks() {
        return drinkRepository.findAll();
    }

}
