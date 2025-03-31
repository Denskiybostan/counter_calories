package com.example.calories.calculation.repository;

import com.example.calories.calculation.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface MealRepository  extends JpaRepository<Meal, Long>, JpaSpecificationExecutor<Meal> {
    List<Meal> findByUserId(Long userId);
}
