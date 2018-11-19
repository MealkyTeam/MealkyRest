package com.mealky.rest.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mealky.rest.model.Category;
import com.mealky.rest.model.Ingredient;
import com.mealky.rest.model.Meal;
import com.mealky.rest.model.MealIngredient;
import com.mealky.rest.model.Unit;
import com.mealky.rest.model.User;
import com.mealky.rest.repository.CategoryRepository;
import com.mealky.rest.repository.IngredientRepository;
import com.mealky.rest.repository.MealRepository;
import com.mealky.rest.repository.UnitRepository;
import com.mealky.rest.repository.UserRepository;

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
//	@GetMapping("/meals")
//	List<Meal> all()
//	{
//		return repository.findAll();
//	}

	@RequestMapping(value="/meals",method=RequestMethod.GET)
	ResponseEntity<Page<Meal>> allPage(@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable)
	{
		Page<Meal> list = repository.findAll(pageable);
		if(list!=null)
		return new ResponseEntity<>(list, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/meals/{id}")
	ResponseEntity<Optional<Meal>> one(@PathVariable long id)
	{
		Optional<Meal> c = repository.findById(id);
		if(c!=null)
			return new ResponseEntity<>(c, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/sec/meals")
	ResponseEntity<HttpStatus> addMeal(@RequestBody Meal meal)
	{
		try {
		User author = urepo.findById(meal.getAuthor().getId()).orElse(new User());
		Set<MealIngredient> mllist = new HashSet<>();
		for(MealIngredient ml : meal.getMealigredient()) {
			MealIngredient tmp = new MealIngredient(meal, ingrepo.findById(
					ml.getIngredient().getId())
					.orElse(new Ingredient(ml.getIngredient().getName())), unitrepo.findById(ml.getUnit().getId()).orElse(new Unit(ml.getUnit().getName())), ml.getQuantity());
			if(ml.getUnit().getId()<0)
			unitrepo.save(tmp.getUnit());
			if(ml.getIngredient().getId()<0)
			ingrepo.save(tmp.getIngredient());
			mllist.add(tmp);
		}
		
		meal.setAuthor(author);
		Set<Category> categorylist = new HashSet<>();
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
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
