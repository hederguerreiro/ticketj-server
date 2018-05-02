package com.guerreiro.heder.springdatarestticketj.entities;

import java.util.List;

import org.springframework.data.annotation.Id;

public final class Project {

	@Id
	private String id;
	private String name;
	private String description;
	private List<Ticket> tickets;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	

}
