// src/main/java/com/example/frattab/services/BillingService.java
package com.example.frattab.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

import com.example.frattab.dto.BillingRunDetailDto;
import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.MemberBillDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.DrinkLog;

public interface BillingService {
    /**
     * Calculate each member’s bill for all DrinkLog entries created
     * between the last BillingRun and now. Then persist a new BillingRun with
     * 'now'.
     * 
     * @return a list of MemberBillDto, one per member, showing the amount owed
     *         between [periodStart, periodEnd).
     */
    List<MemberBillDto> runQuarterlyBilling();

    List<DrinkLog> getDrinkLogsBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<BillingRunDetailDto> getBillingRunsForMember(Long memberId, int page);

    List<DrinkLog> getDrinkLogsForCurrentBillingCycleForMember(Long memberId);

    Double getTotalForCurrentBillingCycleForMember(Long memberId);

    ResponseDto markBillIdForMemberIdAsPaid(Long billId, Long memberId);
}
