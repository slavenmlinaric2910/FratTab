package com.example.frattab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.frattab.models.DrinkQty;

@Repository
public interface DrinkQtyRepository extends JpaRepository<DrinkQty, Long> {
    // You can add custom query methods here if needed
    // For example:
    // List<DrinkQty> findByDrinkId(Long drinkId);
}