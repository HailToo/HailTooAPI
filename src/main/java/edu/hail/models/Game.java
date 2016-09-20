package edu.hail.models;

import java.util.List;

public class Game extends GameEntity {
	public List<User> players;
	private GameEntity _room;
	private GameEntity _weapon;
	private GameEntity _suspect;
	
	public boolean isSolution(String roomName, String weaponName, String suspectName) {
		return (_room.name == roomName) 
				&& (_weapon.name == weaponName)
				&& (_suspect.name == suspectName);
	}
}
