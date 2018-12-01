package com.mealky.rest.model.wrapper;

public class TokenWrapper {
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TokenWrapper(String token) {
		super();
		this.token = token;
	}

	public TokenWrapper() {
		super();
	}
	
}
