package com.example.frattab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.frattab.dto.DeliveryDto;
import com.example.frattab.dto.DrinkDto;
import com.example.frattab.dto.MemberDto;
import com.example.frattab.services.MembersService;
import com.example.frattab.services.DrinksService;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class DashboardController {
    @Autowired
    private MembersService membersService;
    @Autowired
    private DrinksService drinksService;

    @GetMapping("/dashboard")
    public String getDashboardPage(Model model) {
        model.addAttribute("drinks", drinksService.getAllDrinks());
        return "./dashboard";
    }

    @GetMapping("/dashboard/members")
    public String getMembersPage(Model model, @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("members", membersService.getAllMembers(page));
        return "./members";
    }

    @PostMapping("/dashboard/members")
    public String addNewMember(@ModelAttribute MemberDto memberDto, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("response", membersService.createNewMember(memberDto));
        return "redirect:/dashboard/members";
    }

    @PostMapping("/dashboard/drink")
    public String addNewDrink(@ModelAttribute DrinkDto drinkDto, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("response", drinksService.addNewDrink(drinkDto));
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/delivery")
    public String addDeliveredDrinks(@ModelAttribute DeliveryDto deliveryDto, RedirectAttributes redirectAttributes) {
        // TODO: process POST request

        return "redirect:/dashboard";
    }

}
