package com.example.frattab.util;

import org.springframework.stereotype.Component;

import com.example.frattab.dto.DrinkDto;
import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkQtyDto;
import com.example.frattab.dto.MemberDto;
import com.example.frattab.models.Drink;
import com.example.frattab.models.DrinkLog;
import com.example.frattab.models.DrinkQty;
import com.example.frattab.models.Member;

@Component
public class Mappers {
    public Member memberDtoToMember(MemberDto memberDto) {
        Member member = new Member();
        member.setFirstName(memberDto.getFirstName());
        member.setLastName(memberDto.getLastName());
        member.setNickName(memberDto.getNickName());
        member.setEmail(memberDto.getEmail());
        return member;
    }

    public MemberDto memberToMemberDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(member.getId()); // Include the ID
        memberDto.setFirstName(member.getFirstName());
        memberDto.setLastName(member.getLastName());
        memberDto.setNickName(member.getNickName());
        memberDto.setEmail(member.getEmail());
        return memberDto;
    }

    public Drink drinkDtoToDrink(DrinkDto drinkDto) {
        Drink drink = new Drink();
        drink.setName(drinkDto.getName());
        return drink;
    }

    public DrinkDto drinkToDrinkDto(Drink drink) {
        DrinkDto drinkDto = new DrinkDto();
        drinkDto.setId(drink.getId());
        drinkDto.setName(drink.getName());
        drinkDto.setQty(drink.getQty());
        drinkDto.setPrice(drink.getPrice());
        return drinkDto;
    }

    public DrinkQtyDto drinkQtyToDrinkQtyDto(DrinkQty drinkQty) {
        DrinkQtyDto drinkQtyDto = new DrinkQtyDto();
        drinkQtyDto.setId(drinkQty.getId());
        drinkQtyDto.setQty(drinkQty.getQty());
        drinkQtyDto.setDrinkId(drinkQty.getDrink().getId());

        return drinkQtyDto;
    }

    public DrinkLogDto drinkLogToDrinkLogDto(DrinkLog drinkLog){
        DrinkLogDto drinkLogDto = new DrinkLogDto();
        drinkLogDto.setMemberId(drinkLog.getId());
        drinkLogDto.setDrinkQuantities(drinkLog.getDrinkQuantities().stream()
            .map(this::drinkQtyToDrinkQtyDto)
            .toList());
        return drinkLogDto;
    }
}
