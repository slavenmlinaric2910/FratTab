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
