package com.example.calories.calculation.service;

import com.example.calories.calculation.model.Gender;
import com.example.calories.calculation.model.Goal;
import com.example.calories.calculation.repository.UserRepository;
import com.example.calories.calculation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.calories.calculation.specifications.UserSpecification;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    public List<User> getUsers(String goal, String email) {
        Specification<User> spec = Specification.where(null);

        if (goal != null) {
            spec = spec.and(UserSpecification.hasGoal(goal));
        }
        if (email != null) {
            spec = spec.and(UserSpecification.hasEmail(email));
        }
        return repository.findAll(spec);
    }
    // Метод для расчета дневной нормы калорий
    public double calculateDailyCalories(User user) {
        if (user.getAge() <= 0 || user.getWeight() <= 0 || user.getHeight() <= 0) {
            throw new IllegalArgumentException("Возраст, вес и рост должны быть больше 0.");
        }

        double bmr;
        if (user.getGender() == Gender.MALE) {
            bmr = 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
        } else {
            bmr = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
        }

        double dailyCalories = 0;
        switch (user.getGoal()) {
            case WEIGHT_LOSS:
                dailyCalories = bmr * 1.2; // Минимальная активность
                break;
            case MAINTENANCE:
                dailyCalories = bmr * 1.55; // Умеренная активность
                break;
            case WEIGHT_GAIN:
                dailyCalories = bmr * 1.75; // Высокая активность
                break;
        }
        return dailyCalories;
    }
    public User createUser(User user) {
        if (user.getAge() <= 0 || user.getWeight() <= 0 || user.getHeight() <= 0) {
            throw new IllegalArgumentException("Возраст, вес и рост должны быть больше 0.");
        }
        return repository.save(user);
    }
    // Метод для получения пользователя по ID
    public Optional<User> getUserById(Long userId) {
        return repository.findById(userId);
    }
}
