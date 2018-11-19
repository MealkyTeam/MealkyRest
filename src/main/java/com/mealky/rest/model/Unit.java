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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name="name")
	private String name;
	
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
	public Unit() {
		super();
	}
	public Unit(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Unit(String name) {
		super();
		this.name = name;
	}
	@Override
	public String toString() {
		return "Unit [id=" + id + ", name=" + name + "]";
	}
	
	
}
