// src/main/java/com/example/frattab/controllers/BillingController.java
package com.example.frattab.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.frattab.dto.MemberBillDto;
import com.example.frattab.services.BillingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    /**
     * Hitting GET /admin/run-billing will immediately invoke the
     * runQuarterlyBilling() method and return the list of MemberBillDto.
     * 
     * (This endpoint is meant for testing purposes. You can secure or remove
     * it once you verify that billing works as expected.)
     */
    @GetMapping("/admin/run-billing")
    public List<MemberBillDto> triggerBillingNow() {
        return billingService.runQuarterlyBilling();
    }
}
