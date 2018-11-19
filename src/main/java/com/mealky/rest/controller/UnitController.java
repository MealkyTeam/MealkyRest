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

import com.mealky.rest.model.Unit;
import com.mealky.rest.repository.UnitRepository;

@RestController
public class UnitController {
	@Autowired
	UnitRepository repository;
	
	@GetMapping("/sec/units")
	ResponseEntity<List<Unit>> all()
	{
		List<Unit> list = repository.findAll();
		if(list!=null)
		return new ResponseEntity<>(list, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/sec/units/{id}")
	ResponseEntity<Optional<Unit>> one(@PathVariable long id)
	{
		Optional<Unit> c = repository.findById(id);
		if(c!=null)
			return new ResponseEntity<>(c, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/sec/units")
	ResponseEntity<HttpStatus> addUnit(@RequestBody Unit unit)
	{
		try {
		repository.save(unit);
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}