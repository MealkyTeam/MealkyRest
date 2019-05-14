package com.mealky.rest.model;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="category")
public class Category {
	private long id;
	private String name;
	private Set<Meal> meals = new HashSet<Meal>();
	
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
		this.name = name == null ? name : name.trim().replaceAll("[ \\t\\x0B\\f\\r]+", " ");
	}

	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinTable(name="category_meal",
		joinColumns = { @JoinColumn(name="category_id")},
		inverseJoinColumns = { @JoinColumn(name="meal_id")})
	@JsonIgnoreProperties("categories")
	public Set<Meal> getMeals() {
		return meals;
	}

	public void setMeals(Set<Meal> meals) {
		this.meals = meals;
}
	
	public Category() {
		super();
	}

	public Category(String name) {
		super();
		this.name = name == null ? name : name.trim().replaceAll("[ \\t\\x0B\\f\\r]+", " ");
	}

}
