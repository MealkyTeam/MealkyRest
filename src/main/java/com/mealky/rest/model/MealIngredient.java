package com.mealky.rest.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="meal_ingredient")
public class MealIngredient implements Serializable{

	private static final long serialVersionUID = 7790375849350202175L;
	private Meal meal;
	private Ingredient ingredient;
	private Unit unit;
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

		public MealIngredient(Meal meal, Ingredient ingredient, Unit unit, double quantity) {
			super();
			this.meal = meal;
			this.ingredient = ingredient;
			this.unit = unit;
			this.quantity = quantity;
		}
}
