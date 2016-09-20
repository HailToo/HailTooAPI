package edu.hail.models;

import java.util.ArrayList;
import java.util.List;

public class Room extends GameEntity {
	public int id;
	public String imageUri;
	public List<User> inhabitants = new ArrayList<User>();
}
