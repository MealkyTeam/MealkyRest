package com.mealky.rest.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.mealky.rest.model.wrapper.IngredientWrapper;

public class MealWrapper {
	private long id;
	private String name;
	private int prep_time;
	private String preparation;
	private String images;
	boolean confirmed;
	private User author;
	private Date created;
	private Set<IngredientWrapper> ingredients = new HashSet<>();
	private Set<Category> categories = new HashSet<>();
	public MealWrapper() {
		super();
	}
	
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
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
		this.name = name == null ? name : name.trim().replaceAll("[ \\t\\x0B\\f\\r]+", " ");
	}

	public int getPrep_time() {
		return prep_time;
	}
	public void setPrep_time(int prep_time) {
		this.prep_time = prep_time;
	}

	public String getPreparation() {
		return preparation;
	}
	public void setPreparation(String preparation) {
		this.preparation = preparation == null ? preparation : preparation.trim().replaceAll("[ \\t\\x0B\\f\\r]+", " ");
	}

	public String[] getImages() {
		if(this.images.equals(""))
			return new String[] {};
		return this.images.split(";");
	}
	public void setImages(String[] images) {
		String array="";
		for(String s : images)
			array+=s+";";
		this.images = array;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Set<Category> getCategories() {
		return categories;
	}
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<IngredientWrapper> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<IngredientWrapper> ingredients) {
		this.ingredients = ingredients;
	}
}
