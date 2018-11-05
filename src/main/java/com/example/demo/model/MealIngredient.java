package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="meal_ingredient")
public class MealIngredient implements Serializable{
	@Column(name="meal")
	private Meal meal;
	@Column(name="ingredient")
	private Ingredient ingredient;
	@Column(name="unit")
	private Unit unit;
@Column(name="quantity")
private double quantity;

@Id
@ManyToOne
@JoinColumn(name = "meal")
public Meal getMeal() {
	return meal;
}
public void setMeal(Meal meal) {
	this.meal = meal;
}
@Id
@ManyToOne
@JoinColumn(name = "ingredient")
@JsonIgnoreProperties({"mealingredient"})
public Ingredient getIngredient() {
	return ingredient;
}
public void setIngredient(Ingredient ingredient) {
	this.ingredient = ingredient;
}
@Id
@ManyToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "unit")
public Unit getUnit() {
	return unit;
}
public void setUnit(Unit unit) {
	this.unit = unit;
}
public double getQuantity() {
	return quantity;
}
public void setQuantity(double quantity) {
	this.quantity = quantity;
}

public MealIngredient() {
	super();
}
public MealIngredient(Meal meal, Ingredient ingredient, double quantity) {
	super();
	this.meal = meal;
	this.ingredient = ingredient;
	this.quantity = quantity;
}

public MealIngredient(Ingredient ingredient, Unit unit, double quantity) {
	super();
	this.ingredient = ingredient;
	this.unit = unit;
	this.quantity = quantity;
}

public MealIngredient(Meal meal, Ingredient ingredient, Unit unit, double quantity) {
	super();
	this.meal = meal;
	this.ingredient = ingredient;
	this.unit = unit;
	this.quantity = quantity;
}
@Override
public boolean equals(Object o) {
	if(this == o) return true;
	if(o==null | getClass()!=o.getClass()) return false;
	MealIngredient that = (MealIngredient) o;
	return Objects.equals(meal, that.meal) && Objects.equals(ingredient, that.ingredient);
}

@Override
public int hashCode() {
	return Objects.hash(meal,ingredient);
}
@Override
public String toString() {
	return "MealIngredient [meal=" + meal + ", ingredient=" + ingredient + ", unit=" + unit + ", quantity=" + quantity
			+ "]";
}


}
