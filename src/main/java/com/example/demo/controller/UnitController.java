package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Unit;
import com.example.demo.repository.UnitRepository;

@RestController
public class UnitController {
	@Autowired
	UnitRepository repository;
	
	@GetMapping("/units")
	List<Unit> all()
	{
		return repository.findAll();
	}
	@GetMapping("/units/{id}")
	Optional<Unit> one(@PathVariable long id)
	{
		return repository.findById(id);
	}
	@PostMapping("/units")
	public void addUnit(@RequestBody Unit unit)
	{
		repository.save(unit);
	}
}
