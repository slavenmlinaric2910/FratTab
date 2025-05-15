// src/main/java/com/example/frattab/services/impl/DrinkLogServiceImpl.java
package com.example.frattab.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkQtyDto;
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

    @Autowired private MemberRepository memberRepo;
    @Autowired private DrinkRepository drinkRepo;
    @Autowired private DrinkLogRepository logRepo;

    @Override
    @Transactional
    public void saveDrinkLog(DrinkLogDto dto) {
        Member member = memberRepo.findById(dto.getMemberId())
            .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        DrinkLog log = new DrinkLog();
        log.setMember(member);

        double total = 0;
        for (DrinkQtyDto qDto : dto.getDrinkQuantities()) {
            if (qDto.getQty() <= 0) continue;

            Drink drink = drinkRepo.findById(qDto.getDrinkId())
                .orElseThrow(() -> new EntityNotFoundException("Drink not found"));

            DrinkQty dq = new DrinkQty();
            dq.setDrink(drink);
            dq.setQty(qDto.getQty());
            dq.setTotal(drink.getPrice() * qDto.getQty());

            log.addDrinkQty(dq);
            total += dq.getTotal();
        }

        log.setTotal(total);
        logRepo.save(log);
    }
}
