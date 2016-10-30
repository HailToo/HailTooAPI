package edu.hail.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import edu.hail.models.Board;
import edu.hail.models.Board.Location;
import edu.hail.models.Game;
import edu.hail.models.GameEntity;
import edu.hail.models.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by wes on 4/8/16.
 */
@Controller
@RequestMapping("/api")
public class GameController {
    private static final Map<String, Map<String, GameEntity>> db = new HashMap<String, Map<String, GameEntity>>();
    private static final String TITLE = "Clueless: a HAIL of a ride!";
    
    static {
    	db.put("games", new HashMap<String, GameEntity>());
    }

    @RequestMapping(value="/suspects/{id}", method = RequestMethod.GET)
    public @ResponseBody Board.CHARACTER getSuspect(HttpServletRequest req, @PathVariable int id) {
        return Board.CHARACTER.values()[id];
    }

    @RequestMapping(value="/suspects", method = RequestMethod.GET)
    public @ResponseBody Board.CHARACTER[] getSuspects(HttpServletRequest req) {
    	return Board.CHARACTER.values();
    }
    
    @RequestMapping(value="/weapons/{id}", method = RequestMethod.GET)
    public @ResponseBody Board.WEAPON getWeapon(HttpServletRequest req, @PathVariable int id) {
    	return Board.WEAPON.values()[id];
    }

    @RequestMapping(value="/weapons", method = RequestMethod.GET)
    public @ResponseBody Board.WEAPON[] getWeapons(HttpServletRequest req) {
    	return Board.WEAPON.values();
    }
    
    @RequestMapping(value="/rooms/{id}", method = RequestMethod.GET)
    public @ResponseBody Board.AREA getRoom(HttpServletRequest req, @PathVariable int id) {
    	return Board.AREA.values()[id];
    }
    
    @RequestMapping(value="/rooms/{id}/moves", method = RequestMethod.GET)
    public @ResponseBody List<Board.AREA> getPossibleMoves(HttpServletRequest req, @RequestParam String gameGuid, @PathVariable int id) {
    	List<Board.AREA> moves = new ArrayList<Board.AREA>();
    	
    	Game game = (Game) db.get("games").get(gameGuid);
    	Location currentLocation = game.board.getLocation(Board.AREA.values()[id]);
    	for (Board.AREA potentialMove : currentLocation.neighbors) {
    		Location potentialLocation = game.board.getLocation(potentialMove);
    		if (potentialLocation.capacity() > potentialLocation.occupants.size()) {
    			moves.add(potentialLocation.name);
    		}
    	}
    	return moves;
    }

    @RequestMapping(value="/rooms", method = RequestMethod.GET)
    public @ResponseBody Board.AREA[] getRooms(HttpServletRequest req) {
    	return Board.AREA.values();
    }
    
    @RequestMapping(value="/game/title", method = RequestMethod.GET)
    public @ResponseBody String getTitle(HttpServletRequest req) {
        return TITLE;
    }
    
    /**
     * TODO
     * Player starts an instance of the game. A GUID is generated to
     * represent this particular game and is returned to the user.
     * @param req
     * @return
     */
    @RequestMapping(value="/game", method = RequestMethod.PUT)
    public @ResponseBody String createGame(HttpServletRequest req) {
    	// Create unique game identifier
    	String gameId = UUID.randomUUID().toString();
    	Game newGame = new Game();
    	newGame.name = gameId;
    	db.get("games").put(gameId, newGame);
//    	newGame.players.add(new User(getUserName(req), ""));
    	//Maybe player has to join the game once created?
    	return gameId;
    }
    
    /**
     * TODO
     * All players desiring to join a specific game (with friends) send
     * the GUID representing the game to join.
     * Function checks that the game has an available slot for the player,
     * @param req
     * @param gameGuid
     * @return
     */
    @RequestMapping(value="/game", method = RequestMethod.POST)
    public @ResponseBody Game joinGame(HttpServletRequest req, @RequestParam String gameGuid, @RequestParam Board.CHARACTER characterChoice) {
    	Game game = (Game) db.get("games").get(gameGuid);
    	
    	// Get the requesting user
    	User user = new User(getUserName(req), "");
    	user.character = characterChoice;
    	// TODO - check that character has not already been chosen.
    	game.board.getDefaultLocation(characterChoice).occupants.add(user);
    	
    	// Add to game
    	if (game.players.size() < Game.MAX_PLAYERS) {
    		game.players.add(user);
    	}
    	return game;
    }
    
    /**
     * TODO
     * Player starts an instance of the game. A GUID is generated to
     * represent this particular game and is returned to the user.
     * @param req
     * @return
     */
    @RequestMapping(value="/game/{gameGuid}", method = RequestMethod.GET)
    public @ResponseBody Game getGameInfo(HttpServletRequest req, @PathVariable String gameGuid) {
    	Game game = (Game) db.get("games").get(gameGuid);
    	return game;
    }
    
	/**
	 * TODO
	 * Attempt to solve the mystery for a given game.
	 * @param req
	 * @param roomName
	 * @param weaponName
	 * @param suspectName
	 * @return
	 */
    public @ResponseBody boolean solve(HttpServletRequest req, @PathVariable String gameGuid, @PathVariable String roomName, @PathVariable String weaponName, @PathVariable String suspectName) {
    	Game game = (Game) db.get("games").get(gameGuid);
    	return game.solve(roomName, weaponName, suspectName);
    	//TODO if true - game is finished, notify all players.
    }
    
    public String getUserName(HttpServletRequest req) {
    	String name = "N/A";
    	Cookie[] cookies = req.getCookies();
    	for (Cookie c : cookies) {
    		if(c.getName().equalsIgnoreCase("user_token")) {
    			name = c.getValue();
    		}
    	}
    	
    	return name;
    }
}
