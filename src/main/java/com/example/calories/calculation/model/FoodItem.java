package com.example.calories.calculation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "food_items")
@Getter
@Setter
@NoArgsConstructor
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    private  Meal meal;
    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;
    private  int quantity;

    public FoodItem(Meal meal, Dish dish, int quantity) {
        this.meal = meal;
        this.dish = dish;
        this.quantity = quantity;
    }
    public int getTotalCalories() {
        return dish.getCalories() * quantity;
    }

}
