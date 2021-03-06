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
import edu.hail.models.Board.CHARACTER;
import edu.hail.models.Board.Location;
import edu.hail.models.User.ACTION;

public class Game {
	
	private static final Log log = LogFactory.getLog(Game.class);
	public String name;
	
	public List<User> players;
	private Board.AREA _room;
	private Board.WEAPON _weapon;
	private Board.CHARACTER _suspect;
	
	public Board board;
	
	public static final int MAX_PLAYERS = 6;
	
	private Random random = null;
	
	public int currentMove = 0;
	
	public enum Status {
		// Game has not started or has completed.
		Inactive, 
		
		// Game is ongoing, has been started, not finished. 
		Active,
		
		// Solution proposed, waiting for player to disprove
		Waiting,
		
		Complete
	};
	
	public Status status;
	
	//public SortedMap<Date, String> messages;
	public List<String> messages;
	
	public User getCurrentPlayer() {
		User ret = null;
		if (players.size() > 0) {
			ret = players.get(currentMove % players.size());
			if (ret.availableActions.size() == 0 && ret.isActive) {
				ret.availableActions.add(ACTION.Move);
				if (this.board.getLocation(ret.character).isRoom) {
					ret.availableActions.add(ACTION.Accuse);
				}
			}
		}		
		
		return ret;
	}
	
	public Suggestion currentSuggestion = null;
	
	/**
	 * Create solution
	 */
	public Game() {
		board = new Board();
		players = new ArrayList<User>();
		status = Status.Inactive;
		messages = new ArrayList<String>();
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
		
		// Place MsScarlet at the front of the player list (always starts with her).
		if (this.players.size() > 0 && !this.players.get(0).character.equals(CHARACTER.MsScarlet)) {
			for (Iterator<User> it = this.players.iterator(); it.hasNext();) {
				User p = it.next();
				if (p != null && p.character.equals(CHARACTER.MsScarlet)) {
					this.players.remove(p);
					this.players.add(0, p);
					break;
				}
			}
		}
		
		this.status = Game.Status.Active;
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
		// FIXME: Streams are super cool, but entirely unreadable.
		User ret = null;
		List<User> searchResult = this.players.stream().filter(x->x.name.equals(name)).collect(Collectors.toList());
		if (!searchResult.isEmpty()) {
			ret = searchResult.get(0);
		}
		return ret;
	}
	
	public User getPlayer(Board.CHARACTER character) {
		// FIXME: Streams are super cool, but entirely unreadable.
		User ret = null;
		List<User> searchResult = this.players.stream().filter(x->x.character.equals(character)).collect(Collectors.toList());
		if (!searchResult.isEmpty()) {
			ret = searchResult.get(0);
		}
		return ret;
	}
	
	public boolean movePlayer(User player, Board.AREA moveTo) {
		return movePlayer(player, moveTo, false);
	}
	
	public boolean movePlayer(User player, Board.AREA moveTo, boolean bySuggestion) {
		boolean ret = false;
		
		Location currentLocation = null, futureLocation = null;
    	for(Iterator<Location> it = this.board.getLocations().iterator(); it.hasNext() && (currentLocation == null || futureLocation == null);) {
    		Location l = it.next();
    		if (l.occupants.stream().filter(x->x.name.equals(player.name)).collect(Collectors.toList()).size() == 1) {
    			currentLocation = l;
    		} 
    		if (l.name.equals(moveTo)) {
    			futureLocation = l;
    		}
    	}
    	
    	// Validate that the move is allow
    	if (bySuggestion || (this.getCurrentPlayer().name.equals(player.name) && currentLocation.neighbors.contains(moveTo) && futureLocation.occupants.size() < futureLocation.capacity())) {
    		// Remove player from current location
    		currentLocation.occupants.removeIf(x->x.name.equals(player.name));
    		// Add player to new location
    		futureLocation.occupants.add(player);
    		
    		ret = true;
    	} 
		
		return ret;
	}
	
	public void advanceGame() {
		// Unmark any Wait/Disprove actions
		this.status = Status.Active;
		this.currentSuggestion = null;
		for(User u : this.players) {
			u.availableActions.remove(ACTION.Wait);
			u.availableActions.remove(ACTION.Disprove);
		}
		
		// Next player can always move
		User nextPlayer = null;
		do {
			nextPlayer = getFollowingPlayer(this, this.getCurrentPlayer());
			if (nextPlayer.isActive) {
				nextPlayer.availableActions.add(ACTION.Move);
				if (this.board.getLocation(nextPlayer.character).isRoom) {
					nextPlayer.availableActions.add(ACTION.Accuse);
				}
			}
			currentMove++;
		} while (!nextPlayer.isActive);
		
		if (activeUserCount(this) == 1) {
			// Last player remaining is the winner (by default)
			this.status = Status.Complete;
			messages.add(String.format("[ %s ] has won the game (by default)!", nextPlayer.name));
		}
	}
	
	public static User getFollowingPlayer(Game game, User base) {
    	int nextIndex = 0;
    	for(int i = 0; i < game.players.size(); ++i) {
    		if (game.players.get(i).character.equals(base.character)) {
    			nextIndex = ((i + 1) % game.players.size());
    		}
    	}
    	return game.players.get(nextIndex);
    }
	
	public static int activeUserCount(Game game) {
		int ret = 0;
		
		for(int i = 0; i < game.players.size(); ++i) {
			if (game.players.get(i).isActive) {
				ret++;
			}
		}
		
		return ret;
	}
}
