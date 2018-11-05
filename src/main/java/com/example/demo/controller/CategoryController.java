package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;

@RestController
public class CategoryController {
	@Autowired
	CategoryRepository repository;
	@GetMapping("/categories")
	List<Category> all()
	{
		return repository.findAll();
	}
	@GetMapping("/categories/{id}")
	Optional<Category> one(@PathVariable long id)
	{
		return repository.findById(id);
	}
	@PostMapping("/categories")
	public void addCategory(@RequestBody Category category)
	{
		repository.save(category);
	}
}
