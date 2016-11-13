package edu.hail.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hail.WebUtil;
import edu.hail.models.Board.Location;

public class Game extends GameEntity {
	
	private static final Log log = LogFactory.getLog(Game.class);
	
	public List<User> players;
	private Board.AREA _room;
	private Board.WEAPON _weapon;
	private Board.CHARACTER _suspect;
	
	public Board board;
	
	public static final int MAX_PLAYERS = 6;
	
	private Random random = null;
	
	public int currentMove = 0;
	
	public boolean isActive = false;
	
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
		players = new ArrayList<User>();
	}
	
	public void start() {
		int rand = 0;
		
		List<String> cardDeck = getDeck();
		
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
		
		// Remove the selected cards from the deck
		cardDeck.remove(_room.toString());
		cardDeck.remove(_weapon.toString());
		cardDeck.remove(_suspect.toString());
		
		// Deal out deck
		if (this.players.size() > 0) {
			for(int i = 0; cardDeck.size() > 0; i = (i + 1) % this.players.size()) {
				rand = getRandomInt(cardDeck.size());
				this.players.get(i).cards.add(cardDeck.get(rand));
				cardDeck.remove(rand);
			}
		} else {
			log.warn("A game was started without any players, therefore no cards will be dealt.");
		}
		
		this.isActive = true;
	}
	
	protected List<String> getDeck() {
		List<String> cardDeck = new ArrayList<String>();
		
		for (Location l : board.getLocations()) {
			if (l.isRoom) {
				cardDeck.add(l.name.toString());
			}
		}
		for (Board.CHARACTER c : Board.CHARACTER.values()) {
			cardDeck.add(c.toString());
		}
		for (Board.WEAPON w : Board.WEAPON.values()) {
			cardDeck.add(w.toString());
		}
		
		return cardDeck;
	}
	
	public boolean solve(String roomName, String weaponName, String suspectName) {
		return (_room.toString().equals(roomName)) 
				&& (_weapon.toString().equals(weaponName))
				&& (_suspect.toString().equals(suspectName));
	}
	
	public boolean solve(Board.AREA room, Board.WEAPON weapon, Board.CHARACTER suspect) {
		return (_room.equals(room)) 
				&& (_weapon.equals(weapon))
				&& (_suspect.equals(suspect));
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
		this.players.add(user);
	}
	
	public User getPlayer(HttpServletRequest req) {
		return getPlayer(WebUtil.getUserName(req));
	}
	
	public User getPlayer(String name) {
		return this.players.stream().filter(x->x.name.equals(name)).collect(Collectors.toList()).get(0);
	}
	
	public boolean movePlayer(User player, Board.AREA moveTo) {
		boolean ret = false;
		
		Location currentLocation = null, futureLocation = null;
    	for(Iterator<Location> it = this.board.getLocations().iterator(); it.hasNext() && (currentLocation == null || futureLocation == null);) {
    		Location l = it.next();
    		if (l.occupants.stream().filter(x->x.name.equals(player.name)).collect(Collectors.toList()).size() == 1) {
    			currentLocation = l;
    		} else if (l.name.equals(moveTo)) {
    			futureLocation = l;
    		}
    	}
    	
    	// Validate that the move is allow
    	if (this.getCurrentPlayer().name.equals(player.name) && currentLocation.neighbors.contains(moveTo) && futureLocation.occupants.size() < futureLocation.capacity()) {
    		// Remove player from current location
    		currentLocation.occupants.removeIf(x->x.name.equals(player.name));
    		// Add player to new location
    		futureLocation.occupants.add(player);
    		ret = true;
    	} 
		
		return ret;
	}
}
