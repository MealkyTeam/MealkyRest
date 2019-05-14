package com.mealky.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="unit")
public class Unit {
	private long id;
	private String name;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(unique=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name == null ? name : name.trim().replaceAll("[ \\t\\x0B\\f\\r]+", " ");
	}
	public Unit() {
		super();
	}
	public Unit(String name) {
		super();
		this.name = name == null ? name : name.trim().replaceAll("[ \\t\\x0B\\f\\r]+", " ");
	}
}
