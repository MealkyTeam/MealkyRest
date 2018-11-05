package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@Autowired
	UserRepository repository;
	@GetMapping("/users")
	List<User> all(){
		return repository.findAll();
	}
	@GetMapping("/users/{id}")
	Optional<User> one(@PathVariable long id){
		return repository.findById(id);
	}
	@PostMapping("/users")
	public void addUser(@RequestBody User user)
	{
		repository.save(user);
	}
}
