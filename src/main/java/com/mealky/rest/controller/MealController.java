package com.mealky.rest.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mealky.rest.model.ApiError;
import com.mealky.rest.model.Category;
import com.mealky.rest.model.Ingredient;
import com.mealky.rest.model.Meal;
import com.mealky.rest.model.MealIngredient;
import com.mealky.rest.model.Unit;
import com.mealky.rest.model.User;
import com.mealky.rest.model.wrapper.JsonWrapper;
import com.mealky.rest.model.wrapper.MessageWrapper;
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

    @GetMapping("/meals/{id}")
    ResponseEntity<Optional<Meal>> one(@PathVariable long id) {
        Optional<Meal> c = repository.findById(id);
        if (c != null)
            return new ResponseEntity<>(c, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/meals/category")
    ResponseEntity<Page<Meal>> allByCategoryPage(@RequestParam(name = "id") List<Long> id, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        Page<Meal> list = repository.findDistinctByCategoriesIn(crepo.findAllById(id), pageable);
        if (list != null)
            return new ResponseEntity<>(list, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/meals")
    ResponseEntity<Object> allByNamePage(@RequestParam(name = "q", required = false) String query, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        if (query == null) query = "";
        Page<Meal> list = repository.findDistinctByNameIgnoreCaseLike("%" + query + "%", pageable);
        if (list != null) {
            String s = JsonWrapper.removeFieldsFromPageable(list);
            if (s != null)
                return new ResponseEntity<>(s, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sec/meals")
    ResponseEntity<Object> addMeal(@RequestBody Meal meal) {
    	if(meal.getName()==null || meal.getName().equals(""))
    		return new ResponseEntity<>(
                    new MessageWrapper(ApiError.MEAL_NAME_EMPTY.error()), HttpStatus.CONFLICT);
    	if(meal.getPrep_time()<=0)
    		return new ResponseEntity<>(
                    new MessageWrapper(ApiError.MEAL_PREP_TIME.error()), HttpStatus.CONFLICT);
    	if(meal.getMealingredient().size()<=0)
    		return new ResponseEntity<>(
                    new MessageWrapper(ApiError.MEAL_INGREDIENT.error()), HttpStatus.CONFLICT);
      	if(meal.getImages().length<=0 || meal.getImages().length>=6)
    		return new ResponseEntity<>(
                    new MessageWrapper(ApiError.MEAL_IMAGE.error()), HttpStatus.CONFLICT);
      	if(meal.getPreparation()==null || meal.getPreparation().length()<=0)
    		return new ResponseEntity<>(
                    new MessageWrapper(ApiError.MEAL_DESCRIPTION.error()), HttpStatus.CONFLICT);
        try {
            User author = urepo.findById(meal.getAuthor().getId()).orElse(new User());
            Set<MealIngredient> mllist = new HashSet<>();
            Ingredient tmpIngredient = new Ingredient();
            for (MealIngredient ml : meal.getMealingredient()) {
            	
                MealIngredient tmp = new MealIngredient(meal,
                		ingrepo.findById(ml.getIngredient().getId()).orElse(new Ingredient(ml.getIngredient().getName())), 
                		unitrepo.findById(ml.getUnit().getId()).orElse(new Unit(ml.getUnit().getName())), ml.getQuantity());
                if (ml.getUnit().getId() < 0)
                    unitrepo.save(tmp.getUnit());
                if (ml.getIngredient().getId() < 0) {
                	if((tmpIngredient = ingrepo.findDistinctByNameIgnoreCaseLike(ml.getIngredient().getName()))==null) {
                    ingrepo.save(tmp.getIngredient());
                	}else {
                        tmp.setIngredient(tmpIngredient);
                    }
                } 
                mllist.add(tmp);
            }

            meal.setAuthor(author);
            Set<Category> categorylist = new HashSet<>();
            Category tmp = null;
            for (Category c : meal.getCategories()) {
                tmp = crepo.findById(c.getId()).orElse(new Category(c.getName()));
                tmp.getMeals().add(meal);
                categorylist.add(tmp);
            }
            meal.setCategories(categorylist);
            meal.setMealingredient(mllist);
            meal.setCreated(new Date());
            repository.save(meal);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/sec/meals/all")
    ResponseEntity<Object> addAllMeal(@RequestBody Meal[] meals) {
        for (Meal meal : meals) {
        	if(meal.getName()==null || meal.getName().equals(""))
        		return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_NAME_EMPTY.error()), HttpStatus.CONFLICT);
        	if(meal.getPrep_time()<=0)
        		return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_PREP_TIME.error()), HttpStatus.CONFLICT);
        	if(meal.getMealingredient().size()<=0)
        		return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_INGREDIENT.error()), HttpStatus.CONFLICT);
          	if(meal.getImages().length<=0 || meal.getImages().length>=6)
        		return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_IMAGE.error()), HttpStatus.CONFLICT);
          	if(meal.getPreparation()==null || meal.getPreparation().length()<=0)
        		return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_DESCRIPTION.error()), HttpStatus.CONFLICT);
            try {
                User author = urepo.findById(meal.getAuthor().getId()).orElse(new User());
                Set<MealIngredient> mllist = new HashSet<>();
                Ingredient tmpIngredient = new Ingredient();
                for (MealIngredient ml : meal.getMealingredient()) {
                	
                    MealIngredient tmp = new MealIngredient(meal,
                    		ingrepo.findById(ml.getIngredient().getId()).orElse(new Ingredient(ml.getIngredient().getName())), 
                    		unitrepo.findById(ml.getUnit().getId()).orElse(new Unit(ml.getUnit().getName())), ml.getQuantity());
                    if (ml.getUnit().getId() < 0)
                        unitrepo.save(tmp.getUnit());
                    if (ml.getIngredient().getId() < 0) {
                    	if((tmpIngredient = ingrepo.findDistinctByNameIgnoreCaseLike(ml.getIngredient().getName()))==null) {
                        ingrepo.save(tmp.getIngredient());
                    	}else {
                            tmp.setIngredient(tmpIngredient);
                        }
                    } 
                    mllist.add(tmp);
                }

                meal.setAuthor(author);
                Set<Category> categorylist = new HashSet<>();
                Category tmp = null;
                for (Category c : meal.getCategories()) {
                    tmp = crepo.findById(c.getId()).orElse(new Category(c.getName()));
                    tmp.getMeals().add(meal);
                    categorylist.add(tmp);
                }
                meal.setCategories(categorylist);
                meal.setMealingredient(mllist);
                meal.setCreated(new Date());
                repository.save(meal);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
