package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Category;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Meal;
import com.example.demo.model.MealIngredient;
import com.example.demo.model.Unit;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.MealRepository;
import com.example.demo.repository.UnitRepository;
import com.example.demo.repository.UserRepository;

@RestController
public class MealController {
	@Autowired
	MealRepository repository;
	@Autowired
	UserRepository urepo;
	@Autowired
	CategoryRepository crepo;
	@Autowired
	UnitRepository unitrepo;
	@Autowired
	IngredientRepository ingrepo;
	@GetMapping("/meals")
	List<Meal> all()
	{
		return repository.findAll();
	}

	@GetMapping("/meals/{id}")
	Optional<Meal> one(@PathVariable long id)
	{
		return repository.findById(id);
	}
	
	@PostMapping("meals")
	public String addMeal(@RequestBody Meal meal)
	{
		User author = urepo.findById(meal.getAuthor().getId()).orElse(new User());
		List<MealIngredient> mllist = new ArrayList<>();
		for(MealIngredient ml : meal.getMealigredient()) {
			MealIngredient tmp = new MealIngredient(meal, ingrepo.findById(ml.getIngredient().getId()).orElse(new Ingredient(ml.getIngredient().getName())), unitrepo.findById(ml.getUnit().getId()).orElse(new Unit(ml.getUnit().getName())), ml.getQuantity());
			if(ml.getUnit().getId()<0)
			unitrepo.save(tmp.getUnit());
			if(ml.getIngredient().getId()<0)
			ingrepo.save(tmp.getIngredient());
			mllist.add(tmp);
		}
		
		meal.setAuthor(author);
		List<Category> categorylist = new ArrayList<>();
		Category tmp = null;
		for(Category c : meal.getCategories())
		{
					tmp = crepo.findById(c.getId()).orElse(new Category(c.getName()));
					tmp.getMeals().add(meal);
					categorylist.add(tmp);
		}
		meal.setCategories(categorylist);
		meal.setMealigredient(mllist);
		repository.save(meal);
		return meal.toString();
	}
}
