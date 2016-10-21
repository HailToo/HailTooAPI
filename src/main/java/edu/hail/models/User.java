package edu.hail.models;

import java.util.Random;

/**
 * Created by wes on 9/6/16.
 */
public class User {
	public long id;
    public String name;
    public String email;
    public Board.CHARACTER character;
    
    public User(String name, String email) {
    	//Temporary id, will be assigned by db (or maybe just use email?)
        this.id = (new Random()).nextLong();
        this.name = name;
        this.email = email;
    }
}
