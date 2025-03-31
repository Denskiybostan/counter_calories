package com.example.calories.calculation.service;

import com.example.calories.calculation.exception.UserNotFoundException;
import com.example.calories.calculation.model.Meal;
import com.example.calories.calculation.model.User;
import com.example.calories.calculation.repository.MealRepository;
import com.example.calories.calculation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public MealService(MealRepository mealRepository, UserRepository userRepository, UserService userService) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    public List<Meal> getMealsByUserId(Long userId) {
        return mealRepository.findByUserId(userId);
    }

    public Meal addMeal(Long userId, Meal meal) {
        if (meal == null) {
            throw new IllegalArgumentException("Meal cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        meal.setUser(user);
        return mealRepository.save(meal);
    }

    public void deleteMeal(Long mealId) {
        mealRepository.deleteById(mealId);
    }
    public double getDailyCalories(Long userId, LocalDate date) {
        List<Meal> meals = mealRepository.findByUserId(userId);
        double totalCalories = 0;

        for (Meal meal : meals) {
            if (meal.getDate().equals(date)) { // Проверка, что блюдо относится к нужной дате
                totalCalories += meal.calculateTotalCalories(); // Используем метод для подсчета калорий
            }
        }
        return totalCalories;
    }

    // Проверка, уложился ли пользователь в свою дневную норму калорий
    public boolean isUserWithinDailyCalories(Long userId, LocalDate date) {
        // Получаем пользователя по ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Получаем дневную норму калорий для пользователя
        double dailyCalories = userService.calculateDailyCalories(user);

        // Получаем количество потребленных калорий за день
        double consumedCalories = getDailyCalories(userId, date);

        // Проверяем, уложился ли пользователь в дневную норму калорий
        return consumedCalories <= dailyCalories;
    }
}