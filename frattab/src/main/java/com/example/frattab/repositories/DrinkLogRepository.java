package com.example.frattab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.frattab.models.DrinkLog;

@Repository
public interface DrinkLogRepository extends JpaRepository<DrinkLog, Long> {
    // You can add custom query methods here if needed
    // For example:
    // List<DrinkLog> findByMemberId(Long memberId);
}