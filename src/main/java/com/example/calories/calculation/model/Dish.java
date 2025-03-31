package com.example.calories.calculation.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dishes")
@Getter
@Setter
@NoArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Название блюда не может быть пустым")
    private String name;
    @Min(value = 0, message = "Калории не могут быть отрицательными")
    private int calories;
    @Positive(message = "Белки должны быть положительными")
    private double protein;
    @Positive(message = "Жиры должны быть положительными")
    private double fat;
    @Positive(message = "Углеводы должны быть положительными")
    private double carbs;

}
