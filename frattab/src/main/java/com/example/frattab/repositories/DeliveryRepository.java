package com.example.frattab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.frattab.models.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    // Custom query methods can be defined here if needed
    // For example, you can add methods to find deliveries by drink ID or other
    // criteria

}
