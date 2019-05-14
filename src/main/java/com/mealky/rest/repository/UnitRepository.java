package com.mealky.rest.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.Ingredient;
import com.mealky.rest.model.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long> {
	public Page<Unit> findDistinctByNameIgnoreCaseLike(String query,Pageable pageable);
	public Unit findDistinctByNameIgnoreCaseLike(String name);
}
