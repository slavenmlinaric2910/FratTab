// src/main/java/com/example/frattab/config/SchedulerConfig.java
package com.example.frattab.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.frattab.dto.MemberBillDto;
import com.example.frattab.services.BillingService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final BillingService billingService;

    /** 
     * Run every minute (for quick testing). 
     * Cron breakdown: "0 * * * * *" means "at second = 0, every minute, every hour, every day..."
     * Once verified, restore it to "0 0 0 1 1/3 *" for quarterly runs on the 1st of Jan/Apr/Jul/Oct.
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void runQuarterlyBillingJob() {
        List<MemberBillDto> bills = billingService.runQuarterlyBilling();
        System.out.println(">>> [Scheduled] Billing run at " + java.time.LocalDateTime.now());
        for (MemberBillDto bill : bills) {
            System.out.printf(
                "Scheduled: Member %d (%s %s) owes €%.2f [%s – %s]%n",
                bill.getMemberId(),
                bill.getFirstName(),
                bill.getLastName(),
                bill.getAmountOwed(),
                bill.getPeriodStart(),
                bill.getPeriodEnd()
            );
        }
    }
}
