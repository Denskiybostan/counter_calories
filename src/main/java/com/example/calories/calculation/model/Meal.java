package com.example.calories.calculation.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
    @NotNull(message = "Пользователь не может быть пустым")
    private User user;
    @ManyToMany
    @JoinTable(
            name = "meal_dishes",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    @NotNull(message = "Список блюд не может быть пустым")
    private List<Dish> dishes;
    @PastOrPresent(message = "Дата и время должны быть в прошлом или настоящем")
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
