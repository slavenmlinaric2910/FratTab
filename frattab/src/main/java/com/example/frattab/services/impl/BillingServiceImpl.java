// src/main/java/com/example/frattab/services/impl/BillingServiceImpl.java
package com.example.frattab.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.frattab.dto.MemberBillDto;
import com.example.frattab.models.BillingRun;
import com.example.frattab.models.DrinkLog;
import com.example.frattab.models.Member;
import com.example.frattab.repositories.BillingRunRepository;
import com.example.frattab.repositories.DrinkLogRepository;
import com.example.frattab.repositories.MemberRepository;
import com.example.frattab.services.BillingService;
import com.example.frattab.services.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final BillingRunRepository billingRunRepository;
    private final MemberRepository memberRepository;
    private final DrinkLogRepository drinkLogRepository;
    private final EmailService emailService; // ← inject the EmailService interface

    /**
     * Calculate each member’s bill for DrinkLog entries between:
     * • the last BillingRun.runTimestamp (if present), or
     * • (now minus 3 months) if this is the first run.
     *
     * Then persist a new BillingRun (with runTimestamp = now), send each member an
     * email,
     * and return one MemberBillDto per member.
     */
    @Override
    @Transactional
    public List<MemberBillDto> runQuarterlyBilling() {
        // 1. Find the most recent BillingRun (if any)
        Optional<BillingRun> latestRunOpt = billingRunRepository.findFirstByOrderByRunTimestampDesc();

        // 2. Determine periodStart:
        // • If we have a previous run, use that timestamp
        // • Otherwise, now().minusMonths(3)
        LocalDateTime periodStart = latestRunOpt
                .map(BillingRun::getRunTimestamp)
                .orElse(LocalDateTime.now().minusMonths(3));

        // 3. periodEnd = now()
        LocalDateTime periodEnd = LocalDateTime.now();

        // 4. Fetch all members
        List<Member> allMembers = memberRepository.findAll();

        // 5. Prepare a formatter for embedding dates in email
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String startDateString = periodStart.format(dateFormatter);
        String endDateString = periodEnd.format(dateFormatter);

        // 6. For each member, sum up their DrinkLog totals, build DTO, and send email
        List<MemberBillDto> results = new ArrayList<>();

        for (Member member : allMembers) {
            // 6a. Sum of all DrinkLog.total for this member in [periodStart, periodEnd)
            Double rawSum = drinkLogRepository
                    .sumTotalByMemberAndCreatedAtBetween(
                            member.getId(),
                            periodStart,
                            periodEnd);

            double totalDue = (rawSum != null)
                    ? BigDecimal.valueOf(rawSum)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue()
                    : 0.0;

            // 6b. Build the DTO for return value / testing
            MemberBillDto billDto = new MemberBillDto(
                    member.getId(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getEmail(),
                    periodStart,
                    periodEnd,
                    totalDue);
            results.add(billDto);

            // 6c. If they owe > €0.00, send an HTML email invoice
            if (totalDue > 0.0) {
                Map<String, Object> templateVars = new HashMap<>();
                templateVars.put("memberName",
                        member.getFirstName() + " " + member.getLastName());
                templateVars.put("periodStart", startDateString);
                templateVars.put("periodEnd", endDateString);
                templateVars.put("totalDue", String.format(Locale.GERMANY, "%.2f", totalDue));

                // (Optional) You could also pass a list of line‐items if you want to detail
                // them.
                // For now, we send a simple summary. If you do want line‐items, you can query
                // drinkLogRepository.findByMemberIdAndCreatedAtBetween(...) and pass them here.
                System.out.println("DRY RUN: billing for "
                        + member.getEmail() + " = €" + totalDue);
                emailService.sendHtmlEmail(
                        member.getEmail(),
                        "Ihre Quartalsrechnung (" + startDateString + " – " + endDateString + ")",
                        "billing-email", // Thymeleaf template name (billing‐email.html)
                        templateVars);
            }
        }

        // 7. Persist a new BillingRun with runTimestamp = now
        BillingRun newRun = new BillingRun();
        newRun.setRunTimestamp(periodEnd);
        BillingRun savedBillingRun = billingRunRepository.save(newRun);

        // 8. set isBilled to true in DrinkLog
        List<DrinkLog> drinkLogs = getDrinkLogsBetween(periodStart, periodEnd);
        drinkLogs.forEach(e -> {
            e.setBilled(true);
            e.setBillingRun(savedBillingRun);
        });
        drinkLogRepository.saveAll(drinkLogs);

        // 9. Return all the MemberBillDto objects
        return results;
    }

    @Override
    public List<DrinkLog> getDrinkLogsBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return drinkLogRepository.findByCreatedAtBetween(startDate, endDate);
    }
}
