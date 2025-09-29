package com.example.frattab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.frattab.models.Drink;

public interface DrinkRepository extends JpaRepository<Drink, Long> {
    // Custom query methods (if any) can go here
    List<Drink> findAllByIsActiveTrue();
}
