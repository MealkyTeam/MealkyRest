package com.mealky.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mealky.config.ConfigProperties;

@SpringBootApplication
public class MealkyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealkyApplication.class, args);
	}
	@Autowired
	ConfigProperties config;

	@Bean
	public Cloudinary cloudinary()
	{
    	Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
    			  "cloud_name", config.getCloudinaryname(),
    			  "api_key", config.getCloudinaryapikey(),
    			  "api_secret", config.getCloudinaryapisecret()));
    	return cloudinary;
	}
	@Bean
	public ConfigProperties configProperties()
	{
		return new ConfigProperties();
	}
}
