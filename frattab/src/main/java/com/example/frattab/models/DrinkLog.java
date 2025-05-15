package com.example.frattab.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
  @GeneratedValue
  Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;
  private double total;

  @OneToMany(mappedBy = "drinkLog", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DrinkQty> drinkQuantities = new ArrayList<>();

  public void addDrinkQty(DrinkQty dq) {
    dq.setDrinkLog(this);
    drinkQuantities.add(dq);
  }
}
