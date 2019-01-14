package com.mealky.rest.controller;

import java.util.Optional;

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

import com.mealky.rest.model.Ingredient;
import com.mealky.rest.model.wrapper.JsonWrapper;
import com.mealky.rest.repository.IngredientRepository;

@RestController
public class IngredientController {
    @Autowired
    IngredientRepository repository;

    @GetMapping("/sec/ingredients")
    ResponseEntity<Object> all(@RequestParam(name = "q", required = false) String query, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        if (query == null) query = "";
        Page<Ingredient> list = repository.findDistinctByNameIgnoreCaseLike("%" + query + "%", pageable);
        if (list != null) {
            String s = JsonWrapper.removeFieldsFromPageable(list);
            if (s != null)
                return new ResponseEntity<>(s, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/sec/ingredients/{id}")
    ResponseEntity<Optional<Ingredient>> one(@PathVariable long id) {
        Optional<Ingredient> c = repository.findById(id);
        if (c != null)
            return new ResponseEntity<>(c, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sec/ingredients")
    ResponseEntity<HttpStatus> addIngredient(@RequestBody Ingredient ingredient) {
        try {
            repository.save(ingredient);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/sec/ingredients/all")
    ResponseEntity<HttpStatus> addAllIngredient(@RequestBody Ingredient[] ingredient) {
        try {
            for (Ingredient i : ingredient)
                repository.save(i);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
