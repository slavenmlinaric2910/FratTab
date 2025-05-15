package com.example.frattab.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkQtyDto;
import com.example.frattab.models.Drink;
import com.example.frattab.models.Member;
import com.example.frattab.repositories.DrinkRepository;
import com.example.frattab.repositories.MemberRepository;
import com.example.frattab.services.DrinkLogService;
import com.example.frattab.services.DrinksService;
import com.example.frattab.services.MembersService;

@Controller
public class UserDrinkController {
    @Autowired
    private MembersService membersService;
    @Autowired
    private DrinksService drinksService;
    @Autowired
    private DrinkLogService drinkLogService;
    private final MemberRepository memberRepository;
    private final DrinkRepository drinkRepository;

    // Inject the repositories
    public UserDrinkController(MemberRepository memberRepository, DrinkRepository drinkRepository) {
        this.memberRepository = memberRepository;
        this.drinkRepository = drinkRepository;
    }

    @GetMapping("/")
    public String redirectToMembersPage() {
        return "redirect:/members";
    }

    @GetMapping("/members")
    public String getMembersPage(Model model) {
        List<Member> members = memberRepository.findAll(); 
        List<Drink> drinks = drinkRepository.findAll();  
        model.addAttribute("members", members);
        model.addAttribute("drinks", drinks);
        return "./index";
    }

    @GetMapping("/members/{memberId}")
public String showDrinkSelection(@PathVariable Long memberId, Model model) {
    Member member = membersService.getMemberById(memberId);
    List<Drink> drinks = drinksService.getAllDrinks();

    DrinkLogDto drinkLogDto = new DrinkLogDto();

    // Initialize    with one entry per drink, setting drinkId and qty=0
    List<DrinkQtyDto> drinkQtyList = drinks.stream().map(drink -> {
        DrinkQtyDto dq = new DrinkQtyDto();
        dq.setDrinkId(drink.getId());
        dq.setQty(0);
        return dq;
    }).collect(Collectors.toList());

    drinkLogDto.setDrinkQuantities(drinkQtyList);
    drinkLogDto.setMemberId(memberId);

    model.addAttribute("member", member);
    model.addAttribute("drinks", drinks);
    model.addAttribute("drinkLogDto", drinkLogDto);
    return "./drink-selection";
}



    @PostMapping("/members/{id}/log-drinks")
    public String logDrinks(@PathVariable Long id, @ModelAttribute DrinkLogDto dto) {
        dto.setMemberId(id); // ensure ID is passed correctly
        System.out.println("Logging drinks for member ID: " + id);
        System.out.println("Drink quantities: " + dto.getDrinkQuantities());
        System.out.println("DTOOOOOO: " + dto);

        return "redirect:/members";
    }


}