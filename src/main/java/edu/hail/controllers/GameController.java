package edu.hail.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import edu.hail.WebUtil;
import edu.hail.models.Board;
import edu.hail.models.Board.Location;
import edu.hail.models.Game;
import edu.hail.models.GameEntity;
import edu.hail.models.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * REST-ish api to support the HailToo implementation of 
 * the virtual board game "Clue-less".
 * @author wes
 * @since 2016/09/06
 */
@Controller
@RequestMapping("/api")
public class GameController {
	
	private static final Log log = LogFactory.getLog(GameController.class);
	
	/**
	 * Key-value oriented data store.  This object will hold all current game instances
	 * and their current states.
	 */
    private static final Map<String, Map<String, GameEntity>> db = new HashMap<String, Map<String, GameEntity>>();
    
    /**
     * Title of the game.
     */
    private static final String TITLE = "Clueless: a HAIL of a ride!";
    
    /**
     * General on-startup initializations.
     */
    static {
    	db.put("games", new HashMap<String, GameEntity>());
    }

    /**
     * Retrieve a specific suspect (character) object.
     * @param id
     * @return
     */
    @RequestMapping(value="/suspects/{id}", method = RequestMethod.GET)
    public @ResponseBody Board.CHARACTER getSuspect(@PathVariable int id) {
        return Board.CHARACTER.values()[id];
    }

    /**
     * Retrieve all suspect (character) objects.
     * @return
     */
    @RequestMapping(value="/suspects", method = RequestMethod.GET)
    public @ResponseBody Board.CHARACTER[] getSuspects() {
    	return Board.CHARACTER.values();
    }
    
    /**
     * Retrieve a specific weapon object.
     * @param id
     * @return
     */
    @RequestMapping(value="/weapons/{id}", method = RequestMethod.GET)
    public @ResponseBody Board.WEAPON getWeapon(@PathVariable int id) {
    	return Board.WEAPON.values()[id];
    }

    /**
     * Retrieve all weapon objects.
     * @return
     */
    @RequestMapping(value="/weapons", method = RequestMethod.GET)
    public @ResponseBody Board.WEAPON[] getWeapons() {
    	return Board.WEAPON.values();
    }
    
    /**
     * Retrieve a specific room object.
     * @param id
     * @return
     */
    @RequestMapping(value="/rooms/{id}", method = RequestMethod.GET)
    public @ResponseBody Board.AREA getRoom(@PathVariable int id) {
    	return Board.AREA.values()[id];
    }
    
    /**
     * Retrieve list of available areas the player is capable of moving to
     * based on their current location.
     * @param gameGuid - unique game instance identifier.
     * @param id
     * @return
     */
    @RequestMapping(value="/rooms/{id}/moves", method = RequestMethod.GET)
    public @ResponseBody List<Board.AREA> getPossibleMoves(@RequestParam String gameGuid, @PathVariable int id) {
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

    /**
     * Retrieve all room objects.
     * @return
     */
    @RequestMapping(value="/rooms", method = RequestMethod.GET)
    public @ResponseBody Board.AREA[] getRooms() {
    	return Board.AREA.values();
    }
    
    /**
     * META - retrieve game title.
     * @return
     */
    @RequestMapping(value="/game/title", method = RequestMethod.GET)
    public @ResponseBody String getTitle() {
        return TITLE;
    }
    
    /**
     * Player starts an instance of the game. A GUID is generated to
     * represent this particular game and is returned to the user.
     * @return
     */
    @RequestMapping(value="/game", method = RequestMethod.PUT)
    public @ResponseBody String createGame() {
    	// Create unique game identifier
    	String gameId = UUID.randomUUID().toString();
    	Game newGame = new Game();
    	newGame.name = gameId;
    	db.get("games").put(gameId, newGame);
    	
    	log.info(String.format("Game created: [%s]", gameId));
    	return gameId;
    }
    
    /**
     * All players desiring to join a specific game (with friends) send
     * the GUID representing the game to join.
     * Function checks that the game has an available slot for the player.
     * @param req
     * @param gameGuid - unique game instance identifier.
     * @param characterChoice
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/game", method = RequestMethod.POST)
    public @ResponseBody Game joinGame(HttpServletRequest req, @RequestParam String gameGuid, @RequestParam Board.CHARACTER characterChoice) throws Exception {
    	Game game = (Game) db.get("games").get(gameGuid);
    	
    	if (game.players.size() >= Game.MAX_PLAYERS) {
    		throw new Exception("Unable to join game, maximum players reached.");
    	}
    	
    	for (Iterator<User> i = game.players.iterator(); i.hasNext();) {
    		if (i.next().character.equals(characterChoice)) {
    			throw new Exception("Character unavailable, please choose another character.");
    		}
    	}
    	
    	// Get the requesting user
    	User user = WebUtil.getUser(req);
    	user.character = characterChoice;
    	game.board.getDefaultLocation(characterChoice).occupants.add(user);
    	
    	// Add to game
    	game.players.add(user);
    	log.info(String.format("Player [%s] joined game [%s].", user.name, gameGuid));
    	return game;
    }
    
    /**
     * Player starts an instance of the game. A GUID is generated to
     * represent this particular game and is returned to the user.
     * @param req
     * @param gameGuid - unique game instance identifier.
     * @return
     */
    @RequestMapping(value="/game/{gameGuid}", method = RequestMethod.GET)
    public @ResponseBody Game getGameInfo(HttpServletRequest req, @PathVariable String gameGuid) {
    	Game game = (Game) db.get("games").get(gameGuid);
    	if (game == null) {
    		
    	}
    	return game;
    }
    
	/**
	 * TODO
	 * Attempt to solve the mystery for a given game.
	 * @param req
	 * @param gameGuid - unique game instance identifier.
	 * @param roomName
	 * @param weaponName
	 * @param suspectName
	 * @return
	 */
    @RequestMapping(value="/game/{gameGuid}/solve", method = RequestMethod.POST)
    public @ResponseBody boolean solve(HttpServletRequest req, @PathVariable String gameGuid, @RequestBody Board.AREA room, @RequestBody Board.WEAPON weapon, @RequestBody Board.CHARACTER suspect) {
    	Game game = (Game) db.get("games").get(gameGuid);
    	return game.solve(room, weapon, suspect);
    	//TODO if true - game is finished, notify all players.
    }
    
    @RequestMapping(value="/game/{gameGuid}/move", method = RequestMethod.POST)
    public @ResponseBody boolean move(HttpServletRequest req, @PathVariable String gameGuid, @RequestParam Board.AREA area) {
    	boolean ret = false;
    	Game game = (Game) db.get("games").get(gameGuid);
    	User user = game.getPlayer(req);
    	
    	ret = game.movePlayer(user, area);
    		
    	if (!ret) {
    		log.info(String.format("Player [%s] requested an invalid move [%s].", user.name, area.toString()));
    	}
    	
    	return ret;
    }
    
    @RequestMapping(value="/game/{gameGuid}/start", method = RequestMethod.POST)
    public @ResponseBody boolean start(HttpServletRequest req, @PathVariable String gameGuid) {
    	Game game = (Game) db.get("games").get(gameGuid);
    	game.start();
    	return true;
    }
}
