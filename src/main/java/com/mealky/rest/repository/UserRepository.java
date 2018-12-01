package com.mealky.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mealky.rest.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByToken(String token);
	public User findByUsername(String username);
	public User findByEmail(String email);
}
