// src/main/java/com/example/frattab/services/impl/DrinkLogServiceImpl.java
package com.example.frattab.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkQtyDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Drink;
import com.example.frattab.models.DrinkLog;
import com.example.frattab.models.DrinkQty;
import com.example.frattab.models.Member;
import com.example.frattab.repositories.DrinkLogRepository;
import com.example.frattab.repositories.DrinkRepository;
import com.example.frattab.repositories.MemberRepository;
import com.example.frattab.services.DrinkLogService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DrinkLogServiceImpl implements DrinkLogService {

    @Autowired
    private MemberRepository memberRepo;
    @Autowired
    private DrinkRepository drinkRepo;
    @Autowired
    private DrinkLogRepository logRepo;

    @Override
    public DrinkLogDto setDrinkLogDto(List<Drink> drinks, Long memberId) {
        DrinkLogDto drinkLogDto = new DrinkLogDto();
        List<DrinkQtyDto> drinkQtyList = drinks.stream().map(drink -> {
            DrinkQtyDto dq = new DrinkQtyDto();
            dq.setDrinkId(drink.getId());
            dq.setQty(0);
            return dq;
        }).collect(Collectors.toList());
        drinkLogDto.setDrinkQuantities(drinkQtyList);
        drinkLogDto.setMemberId(memberId);
        return drinkLogDto;
    }

    @Override
    public ResponseDto logDrink(DrinkLogDto drinkLogDto) {
        ResponseDto response = new ResponseDto();
        Member member = memberRepo.findById(drinkLogDto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        List<DrinkQty> drinks = drinkLogDto.getDrinkQuantities().stream().map(drinkQtyDto -> {
            DrinkQty drinkQty = new DrinkQty();
            drinkQty.setDrink(drinkRepository.findById(drinkQtyDto.getDrinkId())
                    .orElseThrow(() -> new EntityNotFoundException("Drink not found")).getPrice());
        }).collect(Collectors.toList());

        return response;
    }
}
