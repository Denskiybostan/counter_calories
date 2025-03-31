package com.example.calories.calculation.controller;

import com.example.calories.calculation.model.Meal;
import com.example.calories.calculation.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
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
    public List<Meal> getMealsByUser(@PathVariable Long userId) {
        return mealService.getMealsByUserId(userId);
    }
    @PostMapping("/user/{userId}")
    public ResponseEntity<Meal> addMeal(@PathVariable Long userId, @RequestBody Meal meal) {
        Meal createdMeal = mealService.addMeal(userId, meal);
        return new ResponseEntity<>(createdMeal, HttpStatus.CREATED);
    }
    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long mealId) {
        mealService.deleteMeal(mealId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // Эндпоинт для получения отчета за день
    @GetMapping("/user/{userId}/report")
    public ResponseEntity<String> getDailyReport(@PathVariable Long userId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        double totalCalories = mealService.getDailyCalories(userId, localDate);
        boolean withinLimit = mealService.isUserWithinDailyCalories(userId, localDate);

        String report = String.format("Total calories for %s: %.2f\nUser within daily limit: %b",
                localDate, totalCalories, withinLimit);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
