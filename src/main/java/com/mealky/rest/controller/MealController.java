package com.mealky.rest.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mealky.rest.model.ApiError;
import com.mealky.rest.model.Category;
import com.mealky.rest.model.Ingredient;
import com.mealky.rest.model.Meal;
import com.mealky.rest.model.MealIngredient;
import com.mealky.rest.model.MealWrapper;
import com.mealky.rest.model.Unit;
import com.mealky.rest.model.User;
import com.mealky.rest.model.wrapper.IngredientWrapper;
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
    MealRepository mealRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    CategoryRepository categoryRepo;
    @Autowired
    UnitRepository unitRepo;
    @Autowired
    IngredientRepository ingredientRepo;
    @Autowired
	Cloudinary cloudinary;
    
    @GetMapping("/meals/{id}")
    ResponseEntity<Optional<Meal>> one(@PathVariable long id) {
        Optional<Meal> c = mealRepo.findById(id);
        if (c != null)
            return new ResponseEntity<>(c, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/meals/category")
    ResponseEntity<Page<Meal>> allByCategoryPage(@RequestParam(name = "id") List<Long> id, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        Page<Meal> list = mealRepo.findDistinctByCategoriesIn(categoryRepo.findAllById(id), pageable);
        if (list != null)
            return new ResponseEntity<>(list, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/meals")
    ResponseEntity<Object> allByNamePage(@RequestParam(name = "q", required = false) String query, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        if (query == null) query = "";
        Page<Meal> list = mealRepo.findDistinctByNameIgnoreCaseLike("%" + query + "%", pageable);
        if (list != null) {
            String s = JsonWrapper.removeFieldsFromPageableAndConvertDate(list);
            if (s != null)
                return new ResponseEntity<>(s, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/sec/meals/{id}")
    ResponseEntity<Object> removeMeal(@PathVariable long id){
    	List<String> sets = new ArrayList<>();
    	try{
    		Meal meal =  mealRepo.getOne(id);
    		Set<Category> set = meal.getCategories();
    		for(Category c : set) {
    			c.getMeals().remove(meal);
    		}
    		for(String url : meal.getImages())
    		{
    			int indexSlash = url.lastIndexOf("/")+1;
    			int indexDot = url.lastIndexOf(".");
    			sets.add(url.substring(indexSlash, indexDot));
    			cloudinary.uploader().destroy(url.substring(indexSlash, indexDot), ObjectUtils.asMap("invalidate",true));
    		}
    		mealRepo.delete(meal);
    	}catch(Exception e)
    	{
    		return new ResponseEntity<Object>(sets,HttpStatus.CONFLICT);
    	}
    	return new ResponseEntity<Object>(HttpStatus.OK);
    }
    @PostMapping("/sec/meals")
    ResponseEntity<Object> addMeal(@RequestBody MealWrapper meal) {
    	if(meal.getName()==null || meal.getName().equals(""))
    		return new ResponseEntity<>(
                    new MessageWrapper(ApiError.MEAL_NAME_EMPTY.error()), HttpStatus.CONFLICT);
    	if(meal.getPrep_time()<=0)
    		return new ResponseEntity<>(
                    new MessageWrapper(ApiError.MEAL_PREP_TIME.error()), HttpStatus.CONFLICT);
    	if(meal.getIngredients().size()<=0)
    		return new ResponseEntity<>(
                    new MessageWrapper(ApiError.MEAL_INGREDIENT.error()), HttpStatus.CONFLICT);
      	if(meal.getImages().length<=0 || meal.getImages().length>=6)
    		return new ResponseEntity<>(
                    new MessageWrapper(ApiError.MEAL_IMAGE.error()), HttpStatus.CONFLICT);
      	if(meal.getPreparation()==null || meal.getPreparation().length()<=0)
    		return new ResponseEntity<>(
                    new MessageWrapper(ApiError.MEAL_DESCRIPTION.error()), HttpStatus.CONFLICT);
        try {
            User author = userRepo.findById(meal.getAuthor().getId()).orElse(null);
            if(author == null) {
            	return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_USER.error()), HttpStatus.CONFLICT);
            }
            Meal newMeal = new Meal();
            newMeal.setImages(meal.getImages());
            newMeal.setCreated(new Date());
            newMeal.setCategories(meal.getCategories());
            newMeal.setName(meal.getName());
            newMeal.setPrep_time(meal.getPrep_time());
            newMeal.setPreparation(meal.getPreparation());
            newMeal.setAuthor(author);
            newMeal.setConfirmed(true);
            Set<MealIngredient> mllist = new HashSet<>();
            Ingredient tmpIngredient = new Ingredient();
            Unit tmpUnit = new Unit();
            for (IngredientWrapper ml : meal.getIngredients()) {
            	
                MealIngredient tmp = new MealIngredient(newMeal,
                		ingredientRepo.findById(ml.getId()).orElse(new Ingredient(ml.getName())), 
                		unitRepo.findById(ml.getUnit().getId()).orElse(new Unit(ml.getUnit().getName())), ml.getQuantity());
                if (ml.getUnit().getId() <= 0) {
                	if((tmpUnit =unitRepo.findDistinctByNameIgnoreCaseLike(ml.getUnit().getName()))==null) {
                    unitRepo.save(tmp.getUnit());
                	}else {
                		tmp.setUnit(tmpUnit);
                	}
                }
                if (ml.getId() <= 0) {
                	if((tmpIngredient = ingredientRepo.findDistinctByNameIgnoreCaseLike(ml.getName()))==null) {
                    ingredientRepo.save(tmp.getIngredient());
                	}else {
                        tmp.setIngredient(tmpIngredient);
                    }
                } 
                mllist.add(tmp);
            }
            Set<Category> categorylist = new HashSet<>();
            Category tmp = null;
            for (Category c : meal.getCategories()) {
                tmp = categoryRepo.findById(c.getId()).orElse(new Category(c.getName()));
                tmp.getMeals().add(newMeal);
                categorylist.add(tmp);
            }
            newMeal.setCategories(categorylist);
            newMeal.setMealingredient(mllist);
            mealRepo.save(newMeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/sec/meals/all")
    ResponseEntity<Object> addAllMeal(@RequestBody MealWrapper meals[]) {
        for (MealWrapper meal : meals) {
        	if(meal.getName()==null || meal.getName().equals(""))
        		return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_NAME_EMPTY.error()), HttpStatus.CONFLICT);
        	if(meal.getPrep_time()<=0)
        		return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_PREP_TIME.error()), HttpStatus.CONFLICT);
        	if(meal.getIngredients().size()<=0)
        		return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_INGREDIENT.error()), HttpStatus.CONFLICT);
          	if(meal.getImages().length<=0 || meal.getImages().length>=6)
        		return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_IMAGE.error()), HttpStatus.CONFLICT);
          	if(meal.getPreparation()==null || meal.getPreparation().length()<=0)
        		return new ResponseEntity<>(
                        new MessageWrapper(ApiError.MEAL_DESCRIPTION.error()), HttpStatus.CONFLICT);
            try {
                User author = userRepo.findById(meal.getAuthor().getId()).orElse(null);
                if(author == null) {
                	return new ResponseEntity<>(
                            new MessageWrapper(ApiError.MEAL_USER.error()), HttpStatus.CONFLICT);
                }
                Meal newMeal = new Meal();
                newMeal.setImages(meal.getImages());
                newMeal.setCreated(new Date());
                newMeal.setCategories(meal.getCategories());
                newMeal.setName(meal.getName());
                newMeal.setPrep_time(meal.getPrep_time());
                newMeal.setPreparation(meal.getPreparation());
                newMeal.setAuthor(author);
                newMeal.setConfirmed(true);
                Set<MealIngredient> mllist = new HashSet<>();
                Ingredient tmpIngredient = new Ingredient();
                Unit tmpUnit = new Unit();
                for (IngredientWrapper ml : meal.getIngredients()) {
                    MealIngredient tmp = new MealIngredient(newMeal,
                    		ingredientRepo.findById(ml.getId()).orElse(new Ingredient(ml.getName())), 
                    		unitRepo.findById(ml.getUnit().getId()).orElse(new Unit(ml.getUnit().getName())), ml.getQuantity());
                    if (ml.getUnit().getId() <= 0) {
                    	if((tmpUnit =unitRepo.findDistinctByNameIgnoreCaseLike(ml.getUnit().getName()))==null) {
                        unitRepo.save(tmp.getUnit());
                    	}else {
                    		tmp.setUnit(tmpUnit);
                    	}
                    }
                    if (ml.getId() <= 0) {
                    	if((tmpIngredient = ingredientRepo.findDistinctByNameIgnoreCaseLike(ml.getName()))==null) {
                        ingredientRepo.save(tmp.getIngredient());
                    	}else {
                            tmp.setIngredient(tmpIngredient);
                        }
                    } 
                    mllist.add(tmp);
                }
                Set<Category> categorylist = new HashSet<>();
                Category tmp = null;
                for (Category c : meal.getCategories()) {
                    tmp = categoryRepo.findById(c.getId()).orElse(new Category(c.getName()));
                    tmp.getMeals().add(newMeal);
                    categorylist.add(tmp);
                }
                newMeal.setCategories(categorylist);
                newMeal.setMealingredient(mllist);
                mealRepo.save(newMeal);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}