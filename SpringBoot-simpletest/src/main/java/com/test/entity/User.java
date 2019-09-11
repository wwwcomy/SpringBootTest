package com.test.entity;

public class User {

	private long id;
	private String name;

	public User() {
		this(0, "new user");
	}

	public User(long id2, String string) {
		this.id = id2;
		this.name = string;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User, id=" + this.id + "," + "name=" + this.name;
	}
}
