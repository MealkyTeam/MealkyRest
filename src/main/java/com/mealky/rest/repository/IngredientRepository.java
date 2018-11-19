package com.mealky.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
