package com.guerreiro.heder.springdatarestticketj.enums;

public enum StatusEnum {

	OPENED("OPENED"), IN_PROGRESS("IN_PROGRESS"), FINISHED("FINISHED");

	private final String value;

	private StatusEnum(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}

