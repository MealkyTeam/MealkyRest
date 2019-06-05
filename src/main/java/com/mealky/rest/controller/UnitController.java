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

import com.mealky.rest.model.Unit;
import com.mealky.rest.model.wrapper.JsonWrapper;
import com.mealky.rest.repository.UnitRepository;

@RestController
public class UnitController {
    @Autowired
    UnitRepository repository;

    @GetMapping("/sec/units")
    ResponseEntity<Object> all(@RequestParam(name = "q", required = false) String query, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        if (query == null) query = "";
        Page<Unit> list = repository.findDistinctByNameIgnoreCaseLike("%" + query + "%", pageable);
        if (list != null) {
            String s = JsonWrapper.removeFieldsFromPageable(list);
            if (s != null)
                return new ResponseEntity<>(s, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/sec/units/{id}")
    ResponseEntity<Optional<Unit>> one(@PathVariable long id) {
        Optional<Unit> c = repository.findById(id);
        if (c != null)
            return new ResponseEntity<>(c, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sec/units")
    ResponseEntity<HttpStatus> addUnit(@RequestBody Unit unit) {
        try {
        	if(repository.findDistinctByNameIgnoreCaseLike(unit.getName())==null)
            repository.save(unit);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
