package com.mealky.rest.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.Category;
public interface CategoryRepository extends JpaRepository<Category,Long>{

}
