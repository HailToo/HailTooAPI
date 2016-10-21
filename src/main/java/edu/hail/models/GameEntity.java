package edu.hail.models;

import java.util.ArrayList;
import java.util.List;

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
	
//	public static List<GameEntity> getWeapons() {
//		List<GameEntity> ret = new ArrayList<GameEntity>();
//		
//		ret.add(new GameEntity("",  description))
//		
//		return ret;
//	}
}
