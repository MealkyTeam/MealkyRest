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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="ingredient")
public class Ingredient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name="name")
	private String name;
	
	@OneToMany(mappedBy = "ingredient")
	List<MealIngredient> mealingredient = new ArrayList<>();
	@JsonIgnore
	public List<MealIngredient> getMealingredient() {
		return mealingredient;
	}
	public void setMealingredient(List<MealIngredient> mealingredient) {
		this.mealingredient = mealingredient;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Ingredient(String name) {
		super();
		this.name = name;
	}
	public Ingredient(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Ingredient() {
		super();
	}
	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", name=" + name +"]";
	}
	
}
