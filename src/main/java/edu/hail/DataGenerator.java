package edu.hail;

import java.util.ArrayList;
import java.util.List;

import edu.hail.models.Room;

public class DataGenerator {
	public static Room createRoom(int id, String name, String imageUri) {
		Room ret = new Room();
		ret.id = id;
		ret.name = name;
		ret.imageUri = imageUri;
		return ret;
	}
	
	public static List<Room> createAllRooms() {
		List<Room> ret = new ArrayList<Room>();
		
		ret.add(createRoom(0, "Ballroom", "images/ballroom.png"));
		ret.add(createRoom(1, "Billiard Room", "images/billiard_room.png"));
		ret.add(createRoom(2, "Conservatory", "images/conservatory.png"));
		ret.add(createRoom(3, "Dining Room", "images/dining_room.png"));
		ret.add(createRoom(4, "Hall", "images/hall.png"));
		ret.add(createRoom(5, "Kitchen", "images/kitchen.png"));
		ret.add(createRoom(6, "Library", "images/library.png"));
		ret.add(createRoom(7, "Lounge", "images/lounge.png"));
		ret.add(createRoom(8, "Study", "images/study.png"));
		
		return ret;
	}
}
