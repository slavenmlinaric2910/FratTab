// src/main/java/com/example/frattab/models/Drink.java
package com.example.frattab.models;

public class Drink {
    private Long id;
    private String name;
    private double price;
    
    // Constructors
    public Drink() {}
    
    public Drink(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
}