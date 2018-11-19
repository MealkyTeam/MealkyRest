package com.mealky.rest.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mealky.rest.model.User;
import com.mealky.rest.repository.UserRepository;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@Autowired
	UserRepository repository;
	
	@GetMapping("/sec/users")
	ResponseEntity<List<User>> all()
	{
		List<User> list = repository.findAll();
		if(list!=null)
		return new ResponseEntity<>(list, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/sec/users/{id}")
	ResponseEntity<Optional<User>> one(@PathVariable long id)
	{
		Optional<User> c = repository.findById(id);
		if(c!=null)
			return new ResponseEntity<>(c, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/sec/signup")
	ResponseEntity<Object> addUser(@RequestBody User user)
	{
		try {
		System.out.println(user.getEmail()+" "+user.getPassword()+" "+user.getUsername());
		repository.save(user);
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/sec/login")
	ResponseEntity<String> loginUser(@RequestBody User user)
	{
			User u = repository.findByToken(user.getToken());
			if(u!=null) 
			{
				Duration d = Duration.between(u.getTokenDate().toInstant(), Instant.now());
				if(d.toDays()>30) {
					u.setToken(UUID.randomUUID().toString());
					u.setTokenDate(new Date());
				try {	
					repository.save(u);
					return new ResponseEntity<>(HttpStatus.CREATED);
					}
				catch(Exception e){
						e.printStackTrace();
						return new ResponseEntity<>("Something went wrong.",HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				return new ResponseEntity<>(HttpStatus.OK);
			}
			if(user.getPassword()==null && user.getUsername()==null)
				return new ResponseEntity<>("Invalid token.",HttpStatus.NOT_FOUND);
			
			u = repository.findByUsername(user.getUsername());
			if(u==null) return new ResponseEntity<>("User with this username does not exists.",HttpStatus.NOT_FOUND);
			if(u.getPassword().equals(user.getPassword()))
			{
				u.setToken(UUID.randomUUID().toString());
				u.setTokenDate(new Date());
				try {	
					repository.save(u);
					return new ResponseEntity<>(u.getToken(),HttpStatus.OK);
					}
				catch(Exception e){
						e.printStackTrace();
						return new ResponseEntity<>("Something went wrong.",HttpStatus.INTERNAL_SERVER_ERROR);
					}
			}
			return new ResponseEntity<>("Wrong password.",HttpStatus.UNAUTHORIZED);	
	}
}
