package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="customuser")
public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;
@Column(name="username")
private String username;
@Column(name="password")
private String password;
@Column(name="email")
private String email;


@ManyToMany(fetch = FetchType.LAZY,
cascade = {
		CascadeType.PERSIST,
		CascadeType.MERGE
},
mappedBy ="favourite")
@JsonIgnoreProperties({"users","categories"})
List<Meal> meals = new ArrayList<>();
@JsonIgnore
public List<Meal> getMeals() {
	return meals;
}
public void setMeals(List<Meal> meals) {
	this.meals = meals;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
@JsonIgnore
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
@JsonIgnore
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public User(String username, String password, String email) {
	super();
	this.username = username;
	this.password = password;
	this.email = email;
}
public User() {
	super();
}
public User(long id, String username, String password, String email) {
	super();
	this.id = id;
	this.username = username;
	this.password = password;
	this.email = email;
}
@Override
public String toString() {
	return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + "]";
}

}
