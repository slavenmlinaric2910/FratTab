// src/main/java/com/example/frattab/repositories/BillingRunRepository.java
package com.example.frattab.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.frattab.models.BillingRun;

public interface BillingRunRepository extends JpaRepository<BillingRun, Long> {

    /**
     * Spring Data will automatically limit to a single result:
     *   SELECT * 
     *     FROM billing_run 
     *    ORDER BY run_timestamp DESC 
     *    LIMIT 1
     */
    Optional<BillingRun> findFirstByOrderByRunTimestampDesc();
}
