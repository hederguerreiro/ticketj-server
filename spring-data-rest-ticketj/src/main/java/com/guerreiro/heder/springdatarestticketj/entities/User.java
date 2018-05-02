package com.guerreiro.heder.springdatarestticketj.entities;

import java.util.List;

import org.springframework.data.annotation.Id;

public final class User {

	@Id
	private String id;
	private String fullName;
	private String email;
	private String phone;
	private List<Project> projects;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	

}
