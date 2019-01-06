package com.mealky.rest.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.Category;
import com.mealky.rest.model.Meal;

public interface MealRepository extends JpaRepository<Meal, Long> {

	public Page<Meal> findDistinctByCategoriesIn(List<Category> optional,Pageable pageable);
	public Page<Meal> findDistinctByNameIgnoreCaseLike(String query,Pageable pageable);
}
