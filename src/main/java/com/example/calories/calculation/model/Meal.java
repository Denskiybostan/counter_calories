package com.example.calories.calculation.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToMany
    @JoinTable(
            name = "meal_dishes",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private List<Dish> dishes;
    private LocalDateTime dateTime;

    public Meal(User user, List<Dish> dishes, LocalDateTime dateTime) {
        this.user = user;
        this.dishes = dishes;
        this.dateTime = dateTime;
    }
    public double calculateTotalCalories() {
        return dishes.stream().mapToInt(Dish::getCalories).sum();
    }
    public LocalDate getDate() {
        return this.dateTime.toLocalDate();
    }
}
