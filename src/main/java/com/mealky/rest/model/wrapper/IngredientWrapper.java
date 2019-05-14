package com.mealky.rest.model.wrapper;

import com.mealky.rest.model.MealIngredient;
import com.mealky.rest.model.Unit;

public class IngredientWrapper {
	private long id;
	private String name;
	Unit unit;
	private double quantity;
	
	public IngredientWrapper() {
		super();
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
		this.name = name.trim().replaceAll("[ \\t\\x0B\\f\\r]+", " ");
	}
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
	public IngredientWrapper(MealIngredient mi) {
		super();
		this.id = mi.getIngredient().getId();
		this.name = mi.getIngredient().getName();
		this.unit = mi.getUnit();
		this.quantity = mi.getQuantity();
	}
	
}
