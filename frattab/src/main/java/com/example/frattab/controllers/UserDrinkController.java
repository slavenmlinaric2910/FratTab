package com.example.frattab.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.frattab.models.Drink;
import com.example.frattab.models.Member;
import com.example.frattab.repositories.DrinkRepository;
import com.example.frattab.repositories.MemberRepository;

@Controller
public class UserDrinkController {

    private final MemberRepository memberRepository;
    private final DrinkRepository drinkRepository;

    // Inject the repositories
    public UserDrinkController(MemberRepository memberRepository, DrinkRepository drinkRepository) {
        this.memberRepository = memberRepository;
        this.drinkRepository = drinkRepository;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        // Fetch real members from the database
        List<Member> members = memberRepository.findAll();  // Get all members from the DB

        // Fetch real drinks from the database
        List<Drink> drinks = drinkRepository.findAll();  // Get all drinks from the DB

        // Add the members and drinks to the model
        model.addAttribute("members", members);
        model.addAttribute("drinks", drinks);

        return "./index";  // Return the index page
    }
}
