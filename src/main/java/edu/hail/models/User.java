package edu.hail.models;

/**
 * Created by wes on 9/6/16.
 */
public class User {
    public String name;
    public String email;
    public Board.CHARACTER character;
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
