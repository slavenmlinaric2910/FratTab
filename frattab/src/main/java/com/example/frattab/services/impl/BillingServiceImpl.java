// src/main/java/com/example/frattab/services/impl/BillingServiceImpl.java
package com.example.frattab.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.frattab.dto.MemberBillDto;
import com.example.frattab.models.BillingRun;
import com.example.frattab.models.Member;
import com.example.frattab.repositories.BillingRunRepository;
import com.example.frattab.repositories.DrinkLogRepository;
import com.example.frattab.repositories.MemberRepository;
import com.example.frattab.services.BillingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final BillingRunRepository billingRunRepository;
    private final MemberRepository memberRepository;
    private final DrinkLogRepository drinkLogRepository;

    /**
     * Calculate each member’s bill for DrinkLog entries between:
     *   • the last BillingRun, if any, or
     *   • three months ago, if this is the very first run.
     *
     * Then persist a new BillingRun with runTimestamp = now, and return
     * one MemberBillDto per member.
     */
    @Override
    @Transactional
    public List<MemberBillDto> runQuarterlyBilling() {
        // 1. Find the most recent BillingRun (if any)
        Optional<BillingRun> latestRunOpt =
    billingRunRepository.findFirstByOrderByRunTimestampDesc();


        // 2. Determine periodStart:
        //    • If we have a previous run, use its timestamp.
        //    • Otherwise, use (now minus 3 months).
        LocalDateTime periodStart = latestRunOpt
            .map(BillingRun::getRunTimestamp)
            .orElse(LocalDateTime.now().minusMonths(3));

        // 3. periodEnd = now
        LocalDateTime periodEnd = LocalDateTime.now();

        // 4. Fetch all members
        List<Member> members = memberRepository.findAll();

        var results = new ArrayList<MemberBillDto>();

        for (Member m : members) {
            // 5a. Sum all DrinkLog.total where member.id = m.id
            //     AND createdAt is in [periodStart, periodEnd)
            Double sum = drinkLogRepository
                .sumTotalByMemberAndCreatedAtBetween(m.getId(), periodStart, periodEnd);

            // 5b. Build the DTO
            var dto = new MemberBillDto(
                m.getId(),
                m.getFirstName(),
                m.getLastName(),
                m.getEmail(),
                periodStart,
                periodEnd,
                (sum != null ? sum : 0.0)
            );
            results.add(dto);
        }

        // 6. Save a new BillingRun with runTimestamp = periodEnd
        BillingRun newRun = new BillingRun();
        newRun.setRunTimestamp(periodEnd);
        billingRunRepository.save(newRun);

        // 7. Return all the MemberBillDto objects
        return results;
    }
}
