package com.mealky.rest.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


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
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		@Column(unique=true)
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username == null ? username : username.trim().replaceAll("[ \\t\\x0B\\f\\r]+", " ");
		}
		@Column(length=100)
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		@Column(unique=true)
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email == null ? email : email.trim().replaceAll("[ \\t\\x0B\\f\\r]+", " ");
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