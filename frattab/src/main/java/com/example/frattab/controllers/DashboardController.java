package com.example.frattab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.frattab.models.Member;

@Controller
public class DashboardController {
    @GetMapping("/dashboard")
    public String getDashboardPage() {
        return "./dashboard";
    }

    @GetMapping("/dashboard/members")
    public String getMembersPage(Model model) {
        model.addAttribute("member", new Member());
        return "./members";
    }

}
