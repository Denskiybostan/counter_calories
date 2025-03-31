package com.example.calories.calculation.controller;

import com.example.calories.calculation.model.Meal;
import com.example.calories.calculation.service.MealService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
@Validated
public class MealController {
    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping
    public List<Meal> getAllMeals() {
        return mealService.getAllMeals();
    }

    @GetMapping("/user/{userId}")
    public List<Meal> getMealsByUser(@PathVariable @NotNull Long userId) {
        return mealService.getMealsByUserId(userId);
    }
    @PostMapping("/user/{userId}")
    public ResponseEntity<Meal> addMeal(@PathVariable @NotNull Long userId, @RequestBody @Valid Meal meal) {
        Meal createdMeal = mealService.addMeal(userId, meal);
        return new ResponseEntity<>(createdMeal, HttpStatus.CREATED);
    }
    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable @NotNull Long mealId) {
        mealService.deleteMeal(mealId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // Эндпоинт для получения отчета за день
    @GetMapping("/user/{userId}/report")
    public ResponseEntity<String> getDailyReport(@PathVariable @NotNull Long userId, @RequestParam @NotNull String date) {
        LocalDate localDate = LocalDate.parse(date);
        double totalCalories = mealService.getDailyCalories(userId, localDate);
        boolean withinLimit = mealService.isUserWithinDailyCalories(userId, localDate);

        String report = String.format("Total calories for %s: %.2f\nUser within daily limit: %b",
                localDate, totalCalories, withinLimit);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
