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
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="customuser")
public class User {
		private long id;
		private String username;
		private String password;
		private String email;
		private String token;
		private Date tokenDate;
		private boolean confirmed = false;
		//
		//@ManyToMany(fetch = FetchType.LAZY,
		//cascade = {
		//		CascadeType.PERSIST,
		//		CascadeType.MERGE
		//},
		//mappedBy ="favourite")
		//@JsonIgnoreProperties({"users","categories"})
		//Set<Meal> meals = new HashSet<>();
		//@JsonIgnore
		//public Set<Meal> getMeals() {
		//	return meals;
		//}
		//public void setMeals(Set<Meal> meals) {
		//	this.meals = meals;
		//}
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		//@JsonIgnore
		@Column(length=100)
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		//@JsonIgnore
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public Date getTokenDate() {
			return tokenDate;
		}
		public void setTokenDate(Date tokenDate) {
			this.tokenDate = tokenDate;
		}
		
		public boolean isConfirmed() {
			return confirmed;
		}
		public void setConfirmed(boolean confirmed) {
			this.confirmed = confirmed;
		}
		
		public User() {
			super();
		}
}