package com.mealky.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.Meal;

public interface MealRepository extends JpaRepository<Meal, Long> {

}
