package com.mealky.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.UserConfirmToken;

public interface UserConfirmRepository extends JpaRepository<UserConfirmToken, Long>{
	
	UserConfirmToken findByEmailToken(String emailtoken);
}
