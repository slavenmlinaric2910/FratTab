package com.example.frattab.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkQtyDto;
import com.example.frattab.dto.DrinkSelectionDto;
import com.example.frattab.models.DrinkLog;
import com.example.frattab.models.DrinkQty;
import com.example.frattab.repositories.DrinkLogRepository;
import com.example.frattab.repositories.DrinkRepository;
import com.example.frattab.repositories.MemberRepository;
import com.example.frattab.services.DrinkLogService;
import com.example.frattab.services.DrinksService;
import com.example.frattab.services.MembersService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrinkLogServiceImpl implements DrinkLogService {
  private final MembersService membersService;
  private final DrinksService drinksService;
  private final DrinkLogRepository logRepo;
  private final DrinkRepository     drinkRepo;
  private final MemberRepository memberRepo;

@Override
  @Transactional
  public void logDrinks(DrinkLogDto dto) {
    var member = memberRepo.findById(dto.getMemberId())
                  .orElseThrow(() -> new EntityNotFoundException("Member not found"));

    DrinkLog log = new DrinkLog();
    log.setMemberId(member.getId());

    double logTotal = 0;

    for (DrinkQtyDto q : dto.getDrinkQuantities()) {
      if (q.getQty() <= 0) continue;

      var d = drinkRepo.findById(q.getDrinkId())
                .orElseThrow(() -> new EntityNotFoundException("Drink not found"));

      var dq = new DrinkQty();
      dq.setDrink(d);
      dq.setQty(q.getQty());

      // ← SET the per-item total
      double itemTotal = d.getPrice() * q.getQty();
      dq.setTotal(itemTotal);

      log.addDrinkQty(dq);

      logTotal += itemTotal;
    }

    // ← Set the overall log total
    log.setTotal(logTotal);

    logRepo.save(log);
  }
  @Override
  public DrinkSelectionDto prepareSelection(Long memberId) {
    var member = membersService.getMemberById(memberId);
    var drinks = drinksService.getAllDrinks();

    var dto      = new DrinkLogDto();
    dto.setMemberId(memberId);
    drinks.forEach(d -> {
      var q = new DrinkQtyDto();
      q.setDrinkId(d.getId());
      q.setQty(0);
      dto.getDrinkQuantities().add(q);
    });

    var selection = new DrinkSelectionDto();
    selection.setMember(member);
    selection.setDrinks(drinks);
    selection.setDrinkLogDto(dto);
    return selection;
  }
}
