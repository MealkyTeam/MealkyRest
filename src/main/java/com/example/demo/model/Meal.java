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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="meal")
public class Meal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name="name")
	private String name;
	@Column(name="prep_time")
	private int prep_time;
	@Column(name="preparation",length=5000)
	private String preparation;
	@Column(name="images")
	private String images;
	@Column(name="confirmed")
	boolean confirmed;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "author")
	@JsonIgnoreProperties({"author","meals"})
	private User author;
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	
	public Meal() {
		super();
	}
	
	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinTable(name="users",
		joinColumns = { @JoinColumn(name="user_id")},
		inverseJoinColumns = { @JoinColumn(name="meal_id")})
	@JsonIgnoreProperties({"favourite","meals"})
	List<User> favourite = new ArrayList<>();
	public List<User> getFavourite() {
		return favourite;
	}
	public void setFavourite(List<User> favourite) {
		this.favourite = favourite;
	}

	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			},
			mappedBy ="meals")
	@JsonIgnoreProperties("meals")
	List<Category> categories = new ArrayList<>();
	
	@OneToMany(mappedBy = "meal",cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties("meal")
	List<MealIngredient> mealigredient = new ArrayList<>();
	
	public List<MealIngredient> getMealigredient() {
		return mealigredient;
	}
	public void setMealigredient(List<MealIngredient> mealigredient) {
		this.mealigredient = mealigredient;
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
		this.preparation = preparation;
	}
	public String[] getImages() {
		return images.split(";");
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
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public Meal(long id, String name, int prep_time, String preparation, String images, boolean confirmed, User author,
			List<User> favourite, List<Category> categories, List<MealIngredient> mealigredient) {
		super();
		this.id = id;
		this.name = name;
		this.prep_time = prep_time;
		this.preparation = preparation;
		this.images = images;
		this.confirmed = confirmed;
		this.author = author;
		this.favourite = favourite;
		this.categories = categories;
		this.mealigredient = mealigredient;
	}
	public Meal(String name, int prep_time, String preparation, String images, boolean confirmed, User author,
			List<User> favourite, List<Category> categories, List<MealIngredient> mealigredient) {
		super();
		this.name = name;
		this.prep_time = prep_time;
		this.preparation = preparation;
		this.images = images;
		this.confirmed = confirmed;
		this.author = author;
		this.favourite = favourite;
		this.categories = categories;
		this.mealigredient = mealigredient;
	}
	public Meal(String name, int prep_time, String preparation, String images, boolean confirmed, User author) {
		super();
		this.name = name;
		this.prep_time = prep_time;
		this.preparation = preparation;
		this.images = images;
		this.confirmed = confirmed;
		this.author = author;
	}
	@Override
	public String toString() {
		return "Meal [id=" + id + ", name=" + name + ", prep_time=" + prep_time + ", preparation=" + preparation
				+ ", images=" + images + ", confirmed=" + confirmed + ", author=" + author + ", favourite=" + favourite
				+ ", categories=" + categories + "]";
	}
	
	
}
