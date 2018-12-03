package com.mealky.rest.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="ingredient")
public class Ingredient {
	private long id;
	private String name;
	
	
	private Set<MealIngredient> mealingredient = new HashSet<>();
	@OneToMany(mappedBy = "ingredient")
	@JsonIgnore
	public Set<MealIngredient> getMealingredient() {
		return mealingredient;
	}
	public void setMealingredient(Set<MealIngredient> mealingredient) {
		this.mealingredient = mealingredient;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
