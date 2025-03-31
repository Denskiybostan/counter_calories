package com.example.calories.calculation.service;

import com.example.calories.calculation.model.Gender;
import com.example.calories.calculation.model.Goal;
import com.example.calories.calculation.model.User;
import com.example.calories.calculation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализируем моки

        // Создаем тестового пользователя
        user = new User();
        user.setId(1L);
        user.setGoal(Goal.MAINTENANCE);
        user.setGender(Gender.MALE);
        user.setWeight(75);
        user.setHeight(175);
        user.setAge(30);
    }

    @Test
    public void testCalculateDailyCalories_Male() {
        // Тестируем расчет дневной нормы калорий для мужчины
        double dailyCalories = userService.calculateDailyCalories(user);
        assertEquals(2732.11, dailyCalories, 0.1); // Проверка, что результат соответствует ожиданиям
    }

    @Test
    public void testCreateUser() {
        // Проверка создания пользователя
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        verify(userRepository, times(1)).save(user); // Проверка, что метод save был вызван один раз
    }

    @Test
    public void testGetUserById_UserExists() {
        // Проверка, что пользователь найден по ID
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        // Проверка, что возвращается пустой Optional, если пользователь не найден
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(1L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testGetUsers_WithGoalFilter() {
        // Тестируем фильтрацию пользователей по цели
        String goal = "MAINTENANCE";  // Пример цели
        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(user));

        List<User> users = userService.getUsers(goal, null);

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(goal, users.get(0).getGoal().toString());
    }
}
