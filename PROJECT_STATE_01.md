## FratTab Project State (PROJECT_STATE_01)

### Overview
- **Stack**: Spring Boot (MVC, JPA, Thymeleaf)
- **Modules**:
  - `com.example.frattab.config`: MVC/Scheduler config
  - `com.example.frattab.controllers`: MVC controllers (auth, dashboard/billing, user drink flow, API)
  - `com.example.frattab.dto`: DTOs for drinks, drink logs, billing
  - `com.example.frattab.models`: JPA entities
  - `com.example.frattab.repositories`: Spring Data JPA repositories
  - `com.example.frattab.services` and `impl`: domain services and implementations
  - `com.example.frattab.util`: mapping helpers
- **Resources**: Thymeleaf templates, static assets (css/js/images), i18n bundles, `application.properties`
- **Infra**: `Dockerfile`, `docker-compose.yaml`, migrations folder

### Key Domain Models
```1:24:frattab/src/main/java/com/example/frattab/models/Drink.java
// src/main/java/com/example/frattab/models/Drink.java
package com.example.frattab.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int qty;
    private double price;
    private boolean isActive = true;
}
```

```1:61:frattab/src/main/java/com/example/frattab/models/DrinkLog.java
// src/main/java/com/example/frattab/models/DrinkLog.java
package com.example.frattab.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrinkLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the member who consumed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Total cost of this log (sum of its DrinkQty line‐items)
    private double total;

    // Automatically set when this entity is first saved
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "drinkLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DrinkQty> drinkQuantities = new ArrayList<>();

    private boolean isBilled = false;
    private boolean isPaid = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = true) // optional=true allows null
    @JoinColumn(name = "billing_run_id", nullable = true) // DB column allows NULL
    private BillingRun billingRun;

    public void addDrinkQty(DrinkQty dq) {
        dq.setDrinkLog(this);
        drinkQuantities.add(dq);
    }

}
```

```1:37:frattab/src/main/java/com/example/frattab/models/DrinkQty.java
package com.example.frattab.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// DrinkQty.java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrinkQty {
  @Id
  @GeneratedValue
  Long id;
  private int qty;
  private double total;

  @ManyToOne
  @JoinColumn(name = "drink_log_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private DrinkLog drinkLog;

  @ManyToOne
  @JoinColumn(name = "drink_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Drink drink;
}
```

```1:24:frattab/src/main/java/com/example/frattab/models/Member.java
package com.example.frattab.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
}
```

```1:26:frattab/src/main/java/com/example/frattab/models/BillingRun.java
// src/main/java/com/example/frattab/models/BillingRun.java
package com.example.frattab.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // When this billing run was triggered
    private LocalDateTime runTimestamp;
}
```

```1:31:frattab/src/main/java/com/example/frattab/models/Delivery.java
package com.example.frattab.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drink_id")
    private Drink drink;
    private int qty;
    private double price;
    private double total;
    private LocalDate date = LocalDate.now();
}
```

### Repositories (key methods)
```1:53:frattab/src/main/java/com/example/frattab/repositories/DrinkLogRepository.java
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
```

```1:21:frattab/src/main/java/com/example/frattab/repositories/DrinkRepository.java
package com.example.frattab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.frattab.models.Drink;

public interface DrinkRepository extends JpaRepository<Drink, Long> {
    List<Drink> findAllByIsActiveTrue();
}
```

```1:20:frattab/src/main/java/com/example/frattab/repositories/BillingRunRepository.java
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
```

### Services and Core Flows

#### Drinks (inventory and deliveries)
- `DrinksServiceImpl`:
  - `getAllActiveDrinks`: fetches `Drink.isActive == true`
  - `addDrinkDelivery`: increases inventory, records `Delivery`, sets price = cost + 20% margin (rounded to 2 decimals)
  - `updateDrinkQty`: decrements inventory on consumption
  - `deactivateDrink`: sets `isActive = false`

#### Drink logging (consumption)
- `UserDrinkController`:
  - GET `/members/{memberId}` → uses `prepareSelection` to build form model
  - POST `/members/{memberId}/log-drinks` → calls `logDrinks` and redirects
- `DrinkLogServiceImpl`:
  - `prepareSelection(memberId)`
    - fetches `Member` and active `Drink`s
    - builds `DrinkLogDto` with one `DrinkQtyDto` per drink (qty=0); rounds prices for display
  - `logDrinks(DrinkLogDto)`
    - validates `Member`
    - creates `DrinkLog` with `DrinkQty` line items for non-zero quantities
    - computes and sets `DrinkLog.total`
    - saves log (cascades items) and decrements inventory per line via `updateDrinkQty`
  - `quickDrinkLog(QuickConsumptionDto)`
    - single-item logging convenience; computes total and decrements inventory
  - `updateDrinkLog(DrinkQtyDto)`
    - updates an existing line item; adjusts inventory by the difference

#### Billing (quarterly cycle; mark billed/paid)
- `BillingController`:
  - POST `/dashboard/send-billing` → runs billing, flashes count of emails sent, redirects to dashboard
  - POST `/dashboard/member/{memberId}/bill/{billId}/paid` → marks paid for that run/member
- `BillingServiceImpl`:
  - `runQuarterlyBilling()`
    - determines window `[last run timestamp OR now-3 months, now)`
    - sums `DrinkLog.total` per member via repository method
    - builds `MemberBillDto` for all members
    - sends HTML email (Thymeleaf template `billing-email`) for `amountOwed > 0`
    - creates `BillingRun` with `runTimestamp = now`
    - sets `DrinkLog.isBilled = true` and links `billingRun` for logs in window; saves all
    - returns list of `MemberBillDto`
  - `getBillingRunsForMember(memberId, page)` → pages runs, aggregates member’s logs per run, flags invoiced/paid, sums totals
  - `getDrinkLogsForCurrentBillingCycleForMember(memberId)` → all not yet billed
  - `getTotalForCurrentBillingCycleForMember(memberId)` → aggregated sum for open cycle
  - `markBillIdForMemberIdAsPaid(billId, memberId)` → sets `DrinkLog.isPaid = true` for that run/member

### DTOs (high level)
- `DrinkLogDto`: `memberId`, `List<DrinkQtyDto>`, `total`, flags and linkage
- `DrinkQtyDto`: `id`, `drinkId`, `qty`, `total`
- `QuickConsumptionDto`: `memberId`, `drinkId`, `quantity`
- `MemberBillDto`: member identity, period start/end, `amountOwed`
- `BillingRunDetailDto`: per-run logs, invoiced/paid flags, total, `periodEnd`

### Notes
- Logging uses a few `System.out.println` statements; no `Logger` seen in scanned classes.
- Inventory updates are synchronous on consumption and delivery.
- `DrinkLog.createdAt` is auto-set via `@CreationTimestamp`.
- `DrinkLog.isBilled`/`billingRun` set during billing; `isPaid` via controller endpoint.


