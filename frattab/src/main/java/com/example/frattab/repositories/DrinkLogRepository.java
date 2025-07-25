package com.example.frattab.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.frattab.models.DrinkLog;

@Repository
public interface DrinkLogRepository extends JpaRepository<DrinkLog, Long> {
        // You can add custom query methods here if needed
        // For example:
        // List<DrinkLog> findByMemberId(Long memberId);

        /**
         * Sum the 'total' field of all DrinkLog entries for a given member
         * whose createdAt is >= :start AND < :end. If no matching logs, returns null
         * (or zero).
         */
        @Query("SELECT COALESCE(SUM(dl.total), 0.0) " +
                        "FROM DrinkLog dl " +
                        "WHERE dl.member.id = :memberId " +
                        "  AND dl.createdAt >= :start " +
                        "  AND dl.createdAt < :end")
        Double sumTotalByMemberAndCreatedAtBetween(
                        @Param("memberId") Long memberId,
                        @Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

        List<DrinkLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

        List<DrinkLog> findAllByMemberId(Long memberId);

        List<DrinkLog> findAllByMemberIdAndBillingRunId(Long memberId, Long billingRunId);

        List<DrinkLog> findAllByMemberIdAndIsBilledFalse(Long memberId);

        @Query(value = "SELECT ROUND(CAST(COALESCE(SUM(d.total), 0) AS NUMERIC), 2) FROM drink_log d " +
                        "WHERE d.member_id = :memberId AND d.is_billed = false", nativeQuery = true)
        Double sumAndRoundTotalByMemberIdAndNotBilled(@Param("memberId") Long memberId);

        @Modifying
        @Transactional
        @Query("UPDATE DrinkLog d SET d.isPaid = true WHERE d.billingRun.id = :billingRunId AND d.member.id = :memberId")
        void markAsPaid(@Param("billingRunId") Long billingRunId, @Param("memberId") Long memberId);

}