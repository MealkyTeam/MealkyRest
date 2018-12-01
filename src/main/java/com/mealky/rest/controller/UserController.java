package com.mealky.rest.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mealky.rest.model.User;
import com.mealky.rest.model.wrapper.MessageWrapper;
import com.mealky.rest.model.wrapper.TokenWrapper;
import com.mealky.rest.model.wrapper.UserWrapper;
import com.mealky.rest.repository.UserRepository;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@Autowired
	UserRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
		if(!checkEmail(user.getEmail())) return new ResponseEntity<>(
				new MessageWrapper("This email is invalid."),HttpStatus.CONFLICT);
		if(!checkUsername(user.getUsername())) return new ResponseEntity<>(
				new MessageWrapper("Username should contain only alphanumerics."),HttpStatus.CONFLICT);
		if(!checkPasswordLength(user.getPassword())) return new ResponseEntity<>(
				new MessageWrapper("Password length should be longer than 5."),HttpStatus.CONFLICT);
		if(repository.findByEmail(user.getEmail())!=null) return new ResponseEntity<>(
				new MessageWrapper("Account with this email already exists."),HttpStatus.CONFLICT);
		if(repository.findByUsername(user.getUsername())!=null) return new ResponseEntity<>(
				new MessageWrapper("Account with this username already exists."),HttpStatus.CONFLICT);
		try {
			User u = new User();
			u.setUsername(user.getUsername());
			u.setEmail(user.getEmail());
			u.setPassword(passwordEncoder.encode(user.getPassword()));
			u.setConfirmed(false);
			u.setTokenDate(new Date());
		repository.save(u);
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	private boolean checkEmail(String email)
	{
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(email).matches();
	}
	
	private boolean checkUsername(String username)
	{
		String regex = "^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(username).matches();
	}
	
	private boolean checkPasswordLength(String password)
	{
		return password.length() > 5;
	}
	
	@PostMapping("/sec/login")
	ResponseEntity<Object> loginUser(@RequestBody User user)
	{
		User u;
			if(user.getToken()!=null) {
			u = repository.findByToken(user.getToken());
			if(u!=null) 
			{
				if(!u.isConfirmed()) return new ResponseEntity<>(new MessageWrapper("This account is not confirmed."),HttpStatus.NOT_FOUND);
				Duration d = Duration.between(u.getTokenDate().toInstant(), Instant.now());
				if(d.toDays()>30) {
					u.setToken(UUID.randomUUID().toString());
					u.setTokenDate(new Date());
				try {	
					repository.save(u);
					return new ResponseEntity<>(new UserWrapper(u.getId(),u.getUsername(),u.getEmail()),HttpStatus.CREATED);
					}
				catch(Exception e){
						e.printStackTrace();
						return new ResponseEntity<>(new MessageWrapper("Something went wrong."),HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				return new ResponseEntity<>(new UserWrapper(u.getId(),u.getUsername(),u.getEmail()),HttpStatus.OK);
			}
			}
			if(user.getPassword()==null && user.getEmail()==null)
				return new ResponseEntity<>("Invalid token.",HttpStatus.NOT_FOUND);
			
			u = repository.findByEmail(user.getEmail());
			if(u==null) return new ResponseEntity<>(new MessageWrapper("User with this email does not exists."),HttpStatus.NOT_FOUND);
			if(!u.isConfirmed()) return new ResponseEntity<>(new MessageWrapper("This account is not confirmed."),HttpStatus.NOT_FOUND);
			if(passwordEncoder.matches(user.getPassword(), u.getPassword()))
			{
				if(u.getToken()==null) {
				u.setToken(UUID.randomUUID().toString());
				u.setTokenDate(new Date());
				}
				try {	
					repository.save(u);
					return new ResponseEntity<>(new TokenWrapper(u.getToken()),HttpStatus.OK);
					}
				catch(Exception e){
						e.printStackTrace();
						return new ResponseEntity<>(new MessageWrapper("Something went wrong."),HttpStatus.INTERNAL_SERVER_ERROR);
					}
			}
			return new ResponseEntity<>(new MessageWrapper("Wrong password."),HttpStatus.UNAUTHORIZED);	
	}
}
