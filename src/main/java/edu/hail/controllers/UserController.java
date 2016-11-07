package edu.hail.controllers;

import com.google.gson.Gson;

import edu.hail.models.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by wes on 4/8/16.
 */
@Controller
@RequestMapping("/api/users")
public class UserController {
    private static final Map<Integer, User> db = new HashMap<Integer, User>();
    private Gson serializer = new Gson();

    static {
        db.put(1, new User("Wes", "weswalker125@gmail.com"));
        db.put(2, new User("Chris", "christopher.tarvin@gmail.com"));
        db.put(3, new User("Christina", "ctorpey09@yahoo.com"));
        db.put(4, new User("Jameka", "jhicksvt@gmail.com"));
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public @ResponseBody User getUser(HttpServletRequest req, @PathVariable int id) {
        return db.get(id);
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public @ResponseBody Collection<User> getUsers(HttpServletRequest req) {
        return db.values();
    }

    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<String> addOrUpdateUser(HttpServletRequest req, @PathVariable int id, @RequestBody User artist) {
        ResponseEntity<String> ret = null;
        if(db.containsKey(id)) {
            ret = ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            db.put(id, artist);
            ret = ResponseEntity.status(HttpStatus.CREATED).body(serializer.toJson(artist));
        }
        return ret;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<String> deleteUser(HttpServletRequest req, @PathVariable int id) {
        ResponseEntity<String> ret = null;
        if(db.containsKey(id)) {
            User u = db.remove(id);
            ret = ResponseEntity.status(HttpStatus.OK).body(serializer.toJson(u));
        } else {
            ret = ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ret;
    }
 }
