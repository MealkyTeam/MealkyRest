package com.mealky.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="mealky")
public class ConfigProperties {
	private String cloudinaryname;
	private String cloudinaryapikey;
	private String cloudinaryapisecret;
	private String basicauthlogin;
	private String basicauthpassword;
	public String getCloudinaryname() {
		return cloudinaryname;
	}
	public void setCloudinaryname(String cloudinaryname) {
		this.cloudinaryname = cloudinaryname;
	}
	public String getCloudinaryapikey() {
		return cloudinaryapikey;
	}
	public void setCloudinaryapikey(String cloudinaryapikey) {
		this.cloudinaryapikey = cloudinaryapikey;
	}
	public String getCloudinaryapisecret() {
		return cloudinaryapisecret;
	}
	public void setCloudinaryapisecret(String cloudinaryapisecret) {
		this.cloudinaryapisecret = cloudinaryapisecret;
	}
	public String getBasicauthlogin() {
		return basicauthlogin;
	}
	public void setBasicauthlogin(String basicauthlogin) {
		this.basicauthlogin = basicauthlogin;
	}
	public String getBasicauthpassword() {
		return basicauthpassword;
	}
	public void setBasicauthpassword(String basicauthpassword) {
		this.basicauthpassword = basicauthpassword;
	}
	
}
