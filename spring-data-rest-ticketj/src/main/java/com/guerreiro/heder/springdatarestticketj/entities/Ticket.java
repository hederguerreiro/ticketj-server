package com.guerreiro.heder.springdatarestticketj.entities;

import java.util.UUID;

import org.springframework.data.annotation.Id;

import com.guerreiro.heder.springdatarestticketj.enums.StatusEnum;

public final class Ticket {

	@Id
	private String id;
	private String number = "#" + String.valueOf(UUID.randomUUID().getMostSignificantBits());
	private String summary;
	private String details;
	private StatusEnum status;
	private User user;

	public String getNumber() {
		return number;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}
