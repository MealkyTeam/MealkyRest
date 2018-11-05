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
import com.example.demo.model.Ingredient;
import com.example.demo.repository.IngredientRepository;

@RestController
public class IngredientController {
	@Autowired
	IngredientRepository repository;

	@GetMapping("/ingredients")
	List<Ingredient> all()
	{
		return repository.findAll();
	}
	@GetMapping("/ingredients/{id}")
	Optional<Ingredient> one(@PathVariable long id)
	{
		return repository.findById(id);
	}
	@PostMapping("/ingredients")
	public void addIngredient(@RequestBody Ingredient ingredient)
	{
		repository.save(ingredient);
	}
}
