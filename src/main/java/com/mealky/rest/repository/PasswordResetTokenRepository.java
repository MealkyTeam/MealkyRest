package com.mealky.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
		PasswordResetToken findByToken(String token);
		PasswordResetToken findByUser_EmailIgnoreCaseLike(String email);
}
