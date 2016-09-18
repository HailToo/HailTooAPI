package edu.hail.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import edu.hail.models.GameEntity;

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
    	
       db.get("suspects").put(1, new GameEntity("Professor Peter Plum"));
       db.get("suspects").put(2, new GameEntity("Miss Josephine Scarlet"));
       db.get("suspects").put(3, new GameEntity("Colonel Michael Mustard"));
       db.get("suspects").put(4, new GameEntity("Mrs. Blanche White"));
       db.get("suspects").put(5, new GameEntity("Mrs. Patricia Peacock"));
       db.get("suspects").put(6, new GameEntity("Reverend John Green"));
       
       db.get("rooms").put(1, new GameEntity("Study"));
       db.get("rooms").put(2, new GameEntity("Hall"));
       db.get("rooms").put(3, new GameEntity("Lounge"));
       db.get("rooms").put(4, new GameEntity("Library"));
       db.get("rooms").put(5, new GameEntity("Billiard Room"));
       db.get("rooms").put(6, new GameEntity("Dining Room"));
       db.get("rooms").put(7, new GameEntity("Conservatory"));
       db.get("rooms").put(8, new GameEntity("Ballroom"));
       db.get("rooms").put(9, new GameEntity("Kitchen"));
       
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
}
