package com.mealky.rest.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.Category;
import com.mealky.rest.model.Ingredient;
public interface CategoryRepository extends JpaRepository<Category,Long>{
	public Category findDistinctByNameIgnoreCaseLike(String name);
}
