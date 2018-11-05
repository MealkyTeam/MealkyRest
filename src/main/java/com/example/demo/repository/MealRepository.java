package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Meal;

public interface MealRepository extends JpaRepository<Meal, Long> {

}
