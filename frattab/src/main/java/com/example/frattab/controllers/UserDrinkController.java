package com.example.frattab.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.frattab.models.Drink;
import com.example.frattab.models.Member;

@Controller
public class UserDrinkController {

    @GetMapping("/")
    public String getHomePage(Model model) {
        // Create some random members (in a real application, this would be fetched from a database)
        List<Member> members = generateRandomMembers(100);  // Generate 100 random members
        List<Drink> drinks = Arrays.asList(
            new Drink("Oettinger", 0.60),
            new Drink("Wasser", 0.85),
            new Drink("Normales Bier", 1.00),
            new Drink("Alkoholfreies Bier", 1.10),
            new Drink("Weizen", 1.20),
            new Drink("Premium Bier", 1.30),
            new Drink("Softdrinks", 1.65),
            new Drink("Äppler", 2.00),
            new Drink("Flasche Wein", 6.00)
        );
        
        model.addAttribute("drinks", drinks);
        model.addAttribute("members", members);
        return "./index";
    }

    // Helper method to generate random members
    private List<Member> generateRandomMembers(int count) {
        List<Member> members = new ArrayList<>();
        Random random = new Random();
        String[] firstNames = {"John", "Jane", "Alex", "Max", "Emily", "Anna", "David", "Sophia", "Daniel", "Grace"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Williams", "Jones", "Davis", "Miller", "Wilson", "Moore", "Taylor"};
        String[] nickNames = {"CoolGuy", "SuperWoman", "Speedster", "Ace", "Champ", "Warrior", "Master", "Thunder", "King", "Queen"};

        for (int i = 0; i < count; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String nickName = nickNames[random.nextInt(nickNames.length)];
            String email = (firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com").toLowerCase();
            Member member = new Member();
            member.setId(i + 1); // Set a unique ID for each member
            member.setFirstName(firstName);
            member.setLastName(lastName);
            member.setNickName(nickName);
            member.setEmail(email);

            members.add(member);
        }

        return members;
    }
}
