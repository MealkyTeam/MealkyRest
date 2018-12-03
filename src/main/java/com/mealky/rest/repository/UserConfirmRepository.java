package com.mealky.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.UserConfirm;

public interface UserConfirmRepository extends JpaRepository<UserConfirm, Long>{
	
	UserConfirm findByEmailToken(String emailtoken);
}
