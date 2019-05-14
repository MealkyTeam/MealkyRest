package com.mealky.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByToken(String token);
	public User findDistinctByUsernameIgnoreCaseLike(String username);
	public User findDistinctByEmailIgnoreCaseLike(String email);
}
