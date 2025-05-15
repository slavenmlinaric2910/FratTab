package com.example.frattab.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DrinkQty.java
@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class DrinkQty {
  @Id @GeneratedValue Long id;
  private int qty;
 private double total;
  @ManyToOne @JoinColumn(name="drink_log_id") 
  private DrinkLog drinkLog;

  @ManyToOne @JoinColumn(name="drink_id") 
  private Drink drink;
}
