package com.example.frattab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.frattab.dto.DeliveryRequestDto;
import com.example.frattab.dto.DrinkDto;
import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkQtyDto;
import com.example.frattab.dto.MemberDto;
import com.example.frattab.dto.QuickConsumptionDto;
import com.example.frattab.services.BillingService;
import com.example.frattab.services.DrinkLogService;
import com.example.frattab.services.DrinksService;
import com.example.frattab.services.MembersService;

@Controller
public class DashboardController {
    @Autowired
    private MembersService membersService;
    @Autowired
    private DrinksService drinksService;
    @Autowired
    private DrinkLogService drinkLogService;
    @Autowired
    private BillingService billingService;

    @GetMapping("/dashboard")
    public String getDashboardPage(Model model, @RequestParam(defaultValue = "0") int logsPage) {
        model.addAttribute("drinks", drinksService.getAllDrinks());
        model.addAttribute("members", membersService.getAllMembers());
        model.addAttribute("membersCount", membersService.getMembersCount());
        model.addAttribute("monthlySpending", drinksService.getMonthlyDrinkSpending());
        model.addAttribute("recentLogs", drinkLogService.getRecentDrinkLogs(logsPage));
        model.addAttribute("page", logsPage);

        return "./dashboard";
    }

    @GetMapping("/dashboard/members")
    public String getMembersPage(Model model, @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("members", membersService.getAllMembers(page));
        model.addAttribute("page", page);
        return "./members";
    }

    @GetMapping("/dashboard/member/{memberId}/billing")
    public String getMemberBillingDetailPage(Model model, @PathVariable long memberId,
            @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("member", membersService.getMemberById(memberId));
        model.addAttribute("billings", billingService.getBillingRunsForMember(memberId, page));
        model.addAttribute("currentBillingPeriod",
                billingService.getDrinkLogsForCurrentBillingCycleForMember(memberId));
        model.addAttribute("sumTotalForCurrentBillingPeriod",
                billingService.getTotalForCurrentBillingCycleForMember(memberId));
        return "./billing-detail";
    }

    @GetMapping("/dashboard/settings")
    public String getSettingsPage() {
        return "./settings";
    }

    @PostMapping("/dashboard/drinklog/update")
    public String updateDrinkLog(@ModelAttribute DrinkQtyDto drinkQtyDto, DrinkLogDto drinkLogDto,
            RedirectAttributes redirectAttributes) {
        // TODO: process POST request
        redirectAttributes.addFlashAttribute("response", drinkLogService.updateDrinkLog(drinkQtyDto));
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/members")
    public String addNewMember(@ModelAttribute MemberDto memberDto, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("response", membersService.createNewMember(memberDto));
        return "redirect:/dashboard/members";
    }

    @PostMapping("/dashboard/members/update")
    public String updateMember(@ModelAttribute MemberDto memberDto, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("response", membersService.updateMember(memberDto));
        return "redirect:/dashboard/members";
    }

    @PostMapping("/dashboard/drink")
    public String addNewDrink(@ModelAttribute DrinkDto drinkDto, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("response", drinksService.addNewDrink(drinkDto));
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/delivery")
    public String addDeliveredDrinks(@ModelAttribute DeliveryRequestDto deliveryDto,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("response", drinksService.addDrinkDelivery(deliveryDto));
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/quick-add")
    public String addQuickConsumption(@ModelAttribute QuickConsumptionDto quickConsumptionDto,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("response", drinkLogService.quickDrinkLog(quickConsumptionDto));

        return "redirect:/dashboard";
    }

}
