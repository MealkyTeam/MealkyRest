package com.mealky.rest.model.wrapper;

public class MessageWrapper {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageWrapper(String message) {
		super();
		this.message = message;
	}

	public MessageWrapper() {
		super();
	}
	
}
