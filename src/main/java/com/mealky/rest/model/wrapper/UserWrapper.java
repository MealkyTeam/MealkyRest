package com.mealky.rest.model.wrapper;

public class UserWrapper {
	private long id;
	private String username;
	private String email;
	private String token;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public UserWrapper() {
		super();
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public UserWrapper(long id, String username, String email, String token) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.token = token;
	}
	
}
