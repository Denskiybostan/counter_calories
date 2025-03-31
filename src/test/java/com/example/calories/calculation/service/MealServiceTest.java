package com.example.calories.calculation.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.calories.calculation.exception.UserNotFoundException;
import com.example.calories.calculation.model.*;
import com.example.calories.calculation.repository.MealRepository;
import com.example.calories.calculation.repository.UserRepository;
import com.example.calories.calculation.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MealService mealService;

    private Meal meal;
    private User user;
    private Dish dish;

    @BeforeEach
    public void setUp() {
        // Создаем пользователя
        user = new User();
        user.setId(1L);
        user.setGoal(Goal.MAINTENANCE);
        user.setGender(Gender.MALE);
        user.setWeight(75);
        user.setHeight(175);
        user.setAge(30);

        // Создаем блюдо
        dish = new Dish();
        dish.setId(1L);
        dish.setName("Pasta");
        dish.setCalories(400);

        // Создаем прием пищи с блюдом
        meal = new Meal(user, List.of(dish), LocalDateTime.now());
    }

    @Test
    public void testAddMeal() {
        // Проверка создания нового приема пищи
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.save(any(Meal.class))).thenReturn(meal);

        Meal createdMeal = mealService.addMeal(1L, meal);

        assertNotNull(createdMeal);
        assertEquals(meal.getId(), createdMeal.getId());
        verify(mealRepository, times(1)).save(meal);
    }

    @Test
    public void testAddMeal_UserNotFound() {
        // Проверка, что выбрасывается исключение, если пользователь не найден
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> mealService.addMeal(1L, meal));
    }

    @Test
    public void testGetMealsByUserId() {
        // Проверка получения всех приемов пищи по ID пользователя
        when(mealRepository.findByUserId(1L)).thenReturn(List.of(meal));

        List<Meal> meals = mealService.getMealsByUserId(1L);

        assertNotNull(meals);
        assertFalse(meals.isEmpty());
        assertEquals(1, meals.size());
        assertEquals(meal.getId(), meals.get(0).getId());
    }

    @Test
    public void testDeleteMeal() {
        // Проверка удаления приема пищи
        doNothing().when(mealRepository).deleteById(1L);

        mealService.deleteMeal(1L);

        verify(mealRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteMeal_NotFound() {
        // Проверка, что выбрасывается исключение, если приема пищи не существует
        doThrow(new RuntimeException("Meal not found")).when(mealRepository).deleteById(2L);

        assertThrows(RuntimeException.class, () -> mealService.deleteMeal(2L));
    }

    @Test
    public void testGetDailyCalories() {
        // Тестируем подсчет дневной нормы калорий
        when(mealRepository.findByUserId(1L)).thenReturn(List.of(meal));

        double dailyCalories = mealService.getDailyCalories(1L, meal.getDate());

        assertEquals(400, dailyCalories); // Поскольку блюдо имеет 400 калорий
    }

    @Test
    public void testIsUserWithinDailyCalories() {
        // Мокаем метод поиска пользователя по ID
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.findByUserId(1L)).thenReturn(List.of(meal));
        when(userService.calculateDailyCalories(user)).thenReturn(2000.0);
        boolean result = mealService.isUserWithinDailyCalories(1L, meal.getDate());

        assertTrue(result);  // Поскольку 400 < 2000, результат должен быть true
    }
    @Test
    public void testIsUserWithinDailyCalories_UserNotFound() {
        // Мокаем метод поиска пользователя по ID, возвращаем пустой Optional
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Выполняем проверку, что исключение выбрасывается
        assertThrows(RuntimeException.class, () -> {
            mealService.isUserWithinDailyCalories(1L, meal.getDate());
        });
    }

    @Test
    public void testIsUserWithinDailyCalories_Exceeded() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.findByUserId(1L)).thenReturn(List.of(meal));
        when(userService.calculateDailyCalories(user)).thenReturn(2000.0);
        meal.getDishes().get(0).setCalories(2500);
        boolean result = mealService.isUserWithinDailyCalories(1L, meal.getDate());
        assertFalse(result);  // Поскольку 2500 > 2000, результат должен быть false
    }
}

