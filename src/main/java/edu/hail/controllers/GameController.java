package edu.hail.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import edu.hail.DataGenerator;
import edu.hail.models.Game;
import edu.hail.models.GameEntity;
import edu.hail.models.Room;
import edu.hail.models.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by wes on 4/8/16.
 */
@Controller
@RequestMapping("/api")
public class GameController {
    private static final Map<String, Map<Integer, GameEntity>> db = new HashMap<String, Map<Integer, GameEntity>>();
    private static final String TITLE = "Clueless: a HAIL of a ride!";
    
    static {
    	db.put("suspects", new HashMap<Integer, GameEntity>());
    	db.put("rooms", new HashMap<Integer, GameEntity>());
    	db.put("weapons", new HashMap<Integer, GameEntity>());
    	db.put("games", new HashMap<Integer, GameEntity>());
    	
       db.get("suspects").put(1, new GameEntity("Professor Peter Plum"));
       db.get("suspects").put(2, new GameEntity("Miss Josephine Scarlet"));
       db.get("suspects").put(3, new GameEntity("Colonel Michael Mustard"));
       db.get("suspects").put(4, new GameEntity("Mrs. Blanche White"));
       db.get("suspects").put(5, new GameEntity("Mrs. Patricia Peacock"));
       db.get("suspects").put(6, new GameEntity("Reverend John Green"));
       
       List<Room> rooms = DataGenerator.createAllRooms();
       for(int i = 0; i < rooms.size(); ++i) {
    	   db.get("rooms").put(i+1, rooms.get(i));
       }
       
       db.get("weapons").put(1, new GameEntity("Candlestick"));
       db.get("weapons").put(2, new GameEntity("Knife"));
       db.get("weapons").put(3, new GameEntity("Lead pipe"));
       db.get("weapons").put(4, new GameEntity("Revolver"));
       db.get("weapons").put(5, new GameEntity("Rope"));
       db.get("weapons").put(6, new GameEntity("Wrench"));
       
    }

    @RequestMapping(value="/suspects/{id}", method = RequestMethod.GET)
    public @ResponseBody GameEntity getSuspect(HttpServletRequest req, @PathVariable int id) {
        return db.get("suspects").get(id);
    }

    @RequestMapping(value="/suspects", method = RequestMethod.GET)
    public @ResponseBody Collection<GameEntity> getSuspects(HttpServletRequest req) {
        return db.get("suspects").values();
    }
    
    @RequestMapping(value="/weapons/{id}", method = RequestMethod.GET)
    public @ResponseBody GameEntity getWeapon(HttpServletRequest req, @PathVariable int id) {
        return db.get("weapons").get(id);
    }

    @RequestMapping(value="/weapons", method = RequestMethod.GET)
    public @ResponseBody Collection<GameEntity> getWeapons(HttpServletRequest req) {
        return db.get("weapons").values();
    }
    
    @RequestMapping(value="/rooms/{id}", method = RequestMethod.GET)
    public @ResponseBody GameEntity getRoom(HttpServletRequest req, @PathVariable int id) {
        return db.get("rooms").get(id);
    }

    @RequestMapping(value="/rooms", method = RequestMethod.GET)
    public @ResponseBody Collection<GameEntity> getRooms(HttpServletRequest req) {
        return db.get("rooms").values();
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
    public @ResponseBody String startGame(HttpServletRequest req) {
    	// Create unique game identifier
    	String gameId = UUID.randomUUID().toString();
    	Game newGame = new Game();
    	newGame.name = gameId;
    	newGame.players.add(new User(req.getUserPrincipal().getName(), ""));
    	return null;
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
    public @ResponseBody String joinGame(HttpServletRequest req, @PathVariable String gameGuid) {
    	Game game = (Game) db.get("games").get(gameGuid);
    	
    	// Get the requesting user
    	
    	// Add to game
    	//game.players.add(user);
    	return null;
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
    }
}
