package com.mealky.rest.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mealky.rest.model.wrapper.IngredientWrapper;

@Entity
@Table(name="meal")
public class Meal {
	private long id;
	private String name;
	private int prep_time;
	private String preparation;
	private String images;
	boolean confirmed;
	private User author;
	private Date created;
	private Set<MealIngredient> mealingredient = new HashSet<>();
	private Set<Category> categories = new HashSet<>();
	private Set<IngredientWrapper> ingredients = new HashSet<>();
	
	public Meal() {
		super();
	}
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "author")
	@JsonIgnoreProperties({"author","id","meals","password","email","token","tokenDate","confirmed"})
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	
	
//	@ManyToMany(fetch = FetchType.LAZY,
//			cascade = {
//					CascadeType.PERSIST,
//					CascadeType.MERGE
//			})
//	@JoinTable(name="users",
//		joinColumns = { @JoinColumn(name="user_id")},
//		inverseJoinColumns = { @JoinColumn(name="meal_id")})
//	@JsonIgnoreProperties({"favourite","meals"})
//	Set<User> favourite = new HashSet<>();
//	public Set<User> getFavourite() {
//		return favourite;
//	}
//	public void setFavourite(Set<User> favourite) {
//		this.favourite = favourite;
//	}

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
	public int getPrep_time() {
		return prep_time;
	}
	public void setPrep_time(int prep_time) {
		this.prep_time = prep_time;
	}
	@Column(length=5000)
	public String getPreparation() {
		return preparation;
	}
	public void setPreparation(String preparation) {
		this.preparation = preparation;
	}
	@Column(length=5000)
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
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			},
			mappedBy ="meals")
	@JsonIgnoreProperties("meals")
	public Set<Category> getCategories() {
		return categories;
	}
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	@OneToMany(mappedBy = "meal",cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	public Set<MealIngredient> getMealingredient() {
		return mealingredient;
	}
	@JsonProperty
	public void setMealingredient(Set<MealIngredient> mealingredient) {
		this.mealingredient = mealingredient;
	}
	
	@Transient
	@JsonProperty
	public Set<IngredientWrapper> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<IngredientWrapper> ingredients) {
		this.ingredients = ingredients;
	}
	
	@PostLoad
	public void convertMItoIJ() {
		for(MealIngredient mi : this.mealingredient)
			ingredients.add(new IngredientWrapper(mi));
	}
	
}
