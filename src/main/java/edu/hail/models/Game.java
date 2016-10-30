package edu.hail.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Game extends GameEntity {
	public List<User> players;
	private Board.AREA _room;
	private Board.WEAPON _weapon;
	private Board.CHARACTER _suspect;
	
	public Board board;
	
	public static final int MAX_PLAYERS = 6;
	
	private Random random = null;
	
	public int currentMove = 0;
	
	public User getCurrentPlayer() {
		User ret = null;
		if (players.size() > 0) {
			ret = players.get(currentMove % players.size());
		}
		
		return ret;
	}
	
	/**
	 * Create solution
	 */
	public Game() {
		board = new Board();
		int rand = 0;
		// Select the room
		Board.Location l = null;
		do {
			rand = getRandomInt(board.getLocations().size());
			l = board.getLocations().get(rand);
		} while (!l.isRoom);
		_room = l.name;
		
		// Select the weapon
		rand = getRandomInt(Board.WEAPON.values().length);
		_weapon = Board.WEAPON.values()[rand];
		
		// Select the suspect
		rand = getRandomInt(Board.CHARACTER.values().length);
		_suspect = Board.CHARACTER.values()[rand];
		
		players = new ArrayList<User>();
	}
	
	public boolean solve(String roomName, String weaponName, String suspectName) {
		return (_room.toString().equals(roomName)) 
				&& (_weapon.toString().equals(weaponName))
				&& (_suspect.toString().equals(suspectName));
	}
	
	private int getRandomInt(int max) {
		if (random == null) {
			random = new Random(new Date().getTime());
		}
		
		return random.nextInt(max);
	}
	
	protected Board.CHARACTER getSuspect() {
		return _suspect;
	}
	
	protected Board.WEAPON getWeapon() {
		return _weapon;
	}
	
	protected Board.AREA getRoom() {
		return _room;
	}
	
	public void addPlayer(User user) {
		Board.Location defaultPlayerLocation = board.getDefaultLocation(user.character);
		defaultPlayerLocation.occupants.add(user);
	}
}
