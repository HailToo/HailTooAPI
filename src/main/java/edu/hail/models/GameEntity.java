package edu.hail.models;

public class GameEntity {
	public String name;
	public String description;
	
	public GameEntity() {}
	
	public GameEntity(String name) {
		this.name = name;
	}
	
	public GameEntity(String name, String description) {
		this(name);
		this.description = description;
	}
}
