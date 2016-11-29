package edu.hail.models;

public class Suggestion {
	public User suggester;
	
	public Board.AREA room;
	public Board.WEAPON weapon;
	public Board.CHARACTER suspect;
	
	public boolean containsAny(String item) {
		return containsAny(Board.AREA.fromString(item), Board.WEAPON.fromString(item), Board.CHARACTER.fromString(item));
	}
	
	public boolean containsAny(Board.AREA room, Board.WEAPON weapon, Board.CHARACTER suspect) {
		return ((this.room.equals(room)) || (this.weapon.equals(weapon)) || (this.suspect.equals(suspect)));
	}
}
