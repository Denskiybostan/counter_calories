package com.example.calories.calculation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Прием пищи не может быть null")
    private  Meal meal;
    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    @NotNull(message = "Блюдо не может быть null")
    private Dish dish;
    @Min(value = 1, message = "Количество должно быть положительным числом")
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
