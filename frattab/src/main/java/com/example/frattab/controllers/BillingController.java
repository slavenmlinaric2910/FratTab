// src/main/java/com/example/frattab/controllers/BillingController.java
package com.example.frattab.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.frattab.dto.MemberBillDto;
import com.example.frattab.services.BillingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/dashboard/send-billing")
    public String sendBilling(RedirectAttributes flash) {
        // runQuarterlyBilling() returns one DTO per member,
        // but we only actually send emails for those with amountOwed > 0.
        List<MemberBillDto> bills = billingService.runQuarterlyBilling();

        long emailsSent = bills.stream()
            .filter(b -> b.getAmountOwed() > 0.0)
            .count();

        flash.addFlashAttribute("billingMessage",
            "Billing emails sent to " + emailsSent + " member"
            + (emailsSent == 1 ? "" : "s") + ".");

        return "redirect:/dashboard";
    }
}
