package com.example.frattab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.frattab.models.Member;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.frattab.dto.MemberDto;
import com.example.frattab.services.MembersService;

@Controller
public class DashboardController {
    @Autowired
    private MembersService membersService;

    @GetMapping("/dashboard")
    public String getDashboardPage() {
        return "./dashboard";
    }

    @GetMapping("/dashboard/members")
    public String getMembersPage(Model model) {
        model.addAttribute("members", membersService.getAllMembers(0));
        return "./members";
    }

    @PostMapping("/dashboard/members")
    public String addNewMember(@ModelAttribute MemberDto memberDto, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("response", membersService.createNewMember(memberDto));
        return "redirect:/dashboard/members";
    }

}
