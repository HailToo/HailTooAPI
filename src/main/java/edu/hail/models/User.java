package edu.hail.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wes on 9/6/16.
 */
public class User {
    public String name;
    public String email;
    public Board.CHARACTER character;
    public List<String> cards = new ArrayList<String>();
    public List<ACTION> availableActions = new ArrayList<ACTION>();
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public enum ACTION {
    	Move, Suggest, Accuse, Disprove, Wait
    }
}
