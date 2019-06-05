package com.mealky.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mealky.rest.model.Category;
import com.mealky.rest.repository.CategoryRepository;

@RestController
public class CategoryController {
    @Autowired
    CategoryRepository repository;

    @GetMapping("/sec/categories")
    ResponseEntity<List<Category>> all() {
        List<Category> list = repository.findAll();
        if (list != null)
            return new ResponseEntity<>(list, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/sec/categories/{id}")
    ResponseEntity<Optional<Category>> one(@PathVariable long id) {
        Optional<Category> c = repository.findById(id);
        if (c != null)
            return new ResponseEntity<>(c, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sec/categories")
    ResponseEntity<HttpStatus> addCategory(@RequestBody Category category) {
        try {
        	if(repository.findDistinctByNameIgnoreCaseLike(category.getName())==null)
            repository.save(category);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
