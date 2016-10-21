package edu.hail.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
	
	private static List<Location> locations = null;
	
	static {
		getLocations();
	}
	
	public enum CHARACTER {
		ProfPlum,
		MsScarlet,
		ColMustard,
		MrsWhite,
		MrsPeacock,
		MrGreen
	}
	
	public enum WEAPON {
		Candlestick,
		Knife,
		LeadPipe,
		Revolver,
		Rope,
		Wrench
	}
	
	public enum AREA {
		Study,
		Hall,
		Lounge,
		Library,
		BilliardRoom,
		DiningRoom,
		Conservatory,
		Ballroom,
		Kitchen,
		HW1, HW2, HW3, HW4, HW5, HW6, HW7, HW8, HW9, HW10, HW11, HW12
	}
	
	public static class Location {
		public AREA name;
		public List<AREA> neighbors;
		public boolean isRoom;
		
		public Location(AREA name) {
			this.name = name;
			neighbors = new ArrayList<AREA>();
		}
	}
	
	/**
	 * Generate the list of all possible locations on the board 
	 * and their direct neighbors (location through their doorways). 
	 * @return
	 */
	public static List<Location> getLocations() {
		if (locations == null) {
			
			locations = new ArrayList<Location>();
			
			for(AREA area : AREA.values()) {
				Location l = new Location(area);
				
				switch (area) {
				case Study:
					l.neighbors.add(AREA.HW1);
					l.neighbors.add(AREA.HW3);
					l.neighbors.add(AREA.BilliardRoom);
					l.isRoom = true;
					break;
				case Hall:
					l.neighbors.add(AREA.HW1);
					l.neighbors.add(AREA.HW2);
					l.neighbors.add(AREA.HW4);
					l.isRoom = true;
					break;
				case Lounge:
					l.neighbors.add(AREA.HW2);
					l.neighbors.add(AREA.HW5);
					l.neighbors.add(AREA.BilliardRoom);
					l.isRoom = true;
					break;
				case Library:
					l.neighbors.add(AREA.HW3);
					l.neighbors.add(AREA.HW6);
					l.neighbors.add(AREA.HW8);
					l.isRoom = true;
					break;
				case BilliardRoom:
					l.neighbors.add(AREA.HW4);
					l.neighbors.add(AREA.HW6);
					l.neighbors.add(AREA.HW7);
					l.neighbors.add(AREA.HW9);
					l.isRoom = true;
					break;
				case DiningRoom:
					l.neighbors.add(AREA.HW5);
					l.neighbors.add(AREA.HW7);
					l.neighbors.add(AREA.HW10);
					l.isRoom = true;
					break;
				case Conservatory:
					l.neighbors.add(AREA.HW8);
					l.neighbors.add(AREA.HW11);
					l.neighbors.add(AREA.BilliardRoom);
					l.isRoom = true;
					break;
				case Ballroom:
					l.neighbors.add(AREA.HW9);
					l.neighbors.add(AREA.HW11);
					l.neighbors.add(AREA.HW12);
					l.isRoom = true;
					break;
				case Kitchen:
					l.neighbors.add(AREA.HW10);
					l.neighbors.add(AREA.HW12);
					l.neighbors.add(AREA.BilliardRoom);
					l.isRoom = true;
					break;
				case HW1:
					l.neighbors.add(AREA.Study);
					l.neighbors.add(AREA.Hall);
					break;
				case HW2:
					l.neighbors.add(AREA.Hall);
					l.neighbors.add(AREA.Lounge);
					break;
				case HW3:
					l.neighbors.add(AREA.Study);
					l.neighbors.add(AREA.Library);
					break;
				case HW4:
					l.neighbors.add(AREA.Hall);
					l.neighbors.add(AREA.BilliardRoom);
					break;
				case HW5:
					l.neighbors.add(AREA.Lounge);
					l.neighbors.add(AREA.DiningRoom);
					break;
				case HW6:
					l.neighbors.add(AREA.Library);
					l.neighbors.add(AREA.BilliardRoom);
					break;
				case HW7:
					l.neighbors.add(AREA.BilliardRoom);
					l.neighbors.add(AREA.DiningRoom);
					break;
				case HW8:
					l.neighbors.add(AREA.Library);
					l.neighbors.add(AREA.Conservatory);
					break;
				case HW9:
					l.neighbors.add(AREA.BilliardRoom);
					l.neighbors.add(AREA.Ballroom);
					break;
				case HW10:
					l.neighbors.add(AREA.DiningRoom);
					l.neighbors.add(AREA.Kitchen);
					break;
				case HW11:
					l.neighbors.add(AREA.Conservatory);
					l.neighbors.add(AREA.Ballroom);
					break;
				case HW12:
					l.neighbors.add(AREA.Ballroom);
					l.neighbors.add(AREA.Kitchen);
					break;
				}
				locations.add(l);
			}
		}
		
		return locations;
	}
	

}
