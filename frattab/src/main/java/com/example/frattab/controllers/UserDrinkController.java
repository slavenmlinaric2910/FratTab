package com.example.frattab.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.frattab.models.Drink;
import com.example.frattab.models.Member;
import com.example.frattab.repositories.DrinkRepository;
import com.example.frattab.repositories.MemberRepository;
import com.example.frattab.services.DrinksService;
import com.example.frattab.services.MembersService;

@Controller
public class UserDrinkController {
    @Autowired
    private MembersService membersService;
    @Autowired
    private DrinksService drinksService;
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

        model.addAttribute("member", member);
        model.addAttribute("drinks", drinks);

        return "./drink-selection";
    }
}