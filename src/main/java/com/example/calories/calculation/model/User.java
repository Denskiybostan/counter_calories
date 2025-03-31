package com.example.calories.calculation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Column(unique = true, nullable = false)
    private String email;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    private double weight;
    private double height;
    @Enumerated(EnumType.STRING)
    private Goal goal;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private int dailyCalorieInTake;

    public User(int dailyCalorieInTake, int age, String email, Goal goal, double height, String name, double weight) {
        this.dailyCalorieInTake = dailyCalorieInTake;
        this.age = age;
        this.email = email;
        this.goal = goal;
        this.height = height;
        this.name = name;
        this.weight = weight;
    }
    private int calculateCalories() {
        // Формула Харриса-Бенедикта (для мужчин)
        double bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        switch (goal) {
            case WEIGHT_LOSS:
                return (int) (bmr * 1.2 - 500);
            case MAINTENANCE:
                return (int) (bmr * 1.2);
            case WEIGHT_GAIN:
                return (int) (bmr * 1.2 + 500);
            default:
                return (int) bmr;
        }
    }
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals = new ArrayList<>();

}
