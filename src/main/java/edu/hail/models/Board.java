package edu.hail.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
	
	private List<Location> locations = null;
	
	public List<Location> getLocations() {
		return locations;
	}
	
	public Board() {
		locations = generateLocations();
	}
	
	public enum CHARACTER {
		MsScarlet("MsScarlet"),
		ColMustard("ColMustard"),
		MrsWhite("MrsWhite"),
		MrGreen("MrGreen"),
		MrsPeacock("MrsPeacock"),
		ProfPlum("ProfPlum");
		
		private String text;
		
		CHARACTER(String text) {
			this.text = text;
		}
		
		public String getText() {
			return this.text;
		}
		
		public static CHARACTER fromString(String text) {
			if (text != null && !text.trim().isEmpty()) {
				for(CHARACTER c : CHARACTER.values()) {
					if (text.equalsIgnoreCase(c.text)) {
						return c;
					}
				}
			}
			return null;
		}
	}
	
	public enum WEAPON {
		Candlestick("Candlestick"),
		Knife("Knife"),
		LeadPipe("LeadPipe"),
		Revolver("Revolver"),
		Rope("Rope"),
		Wrench("Wrench");
		
		private String text;
		
		WEAPON(String text) {
			this.text = text;
		}
		
		public String getText() {
			return this.text;
		}
		
		public static WEAPON fromString(String text) {
			if (text != null && !text.trim().isEmpty()) {
				for(WEAPON c : WEAPON.values()) {
					if (text.equalsIgnoreCase(c.text)) {
						return c;
					}
				}
			}
			return null;
		}
	}
	
	public enum AREA {
		Study("Study"),
		Hall("Hall"),
		Lounge("Lounge"),
		Library("Library"),
		BilliardRoom("BilliardRoom"),
		DiningRoom("DiningRoom"),
		Conservatory("Conservatory"),
		Ballroom("Ballroom"),
		Kitchen("Kitchen"),
		HW_SH("HW_SH"), HW_HL("HW_HL"), HW_SL("HW_SL"), HW_HB("HW_HB"), HW_LD("HW_LD"), HW_LB("HW_LB"), HW_BD("HW_BD"), HW_LC("HW_LC"), HW_BB("HW_BB"), HW_DK("HW_DK"), HW_CB("HW_CB"), HW_BK("HW_BK");
	
		private String text;
		
		AREA(String text) {
			this.text = text;
		}
		
		public String getText() {
			return this.text;
		}
		
		public static AREA fromString(String text) {
			if (text != null && !text.trim().isEmpty()) {
				for(AREA c : AREA.values()) {
					if (text.equalsIgnoreCase(c.text)) {
						return c;
					}
				}
			}
			return null;
		}
	}
	
	public static class Location {
		public AREA name;
		public List<AREA> neighbors;
		public boolean isRoom;
		public List<User> occupants;
		
		public Location(AREA name) {
			this.name = name;
			neighbors = new ArrayList<AREA>();
			occupants = new ArrayList<User>();
		}
		
		public int capacity() {
			int ret = 1;
			if (isRoom)
				return 6;
			
			return ret;
		}
	}
	
	public Location getLocation(AREA area) {
		for (Location l : locations) {
			if (l.name.equals(area))
				return l;
		}
		
		return null;
	}
	
	public Location getLocation(CHARACTER occupant) {
		for (Location l : locations) {
			if (l.occupants.stream().anyMatch(x-> x.character.equals(occupant)))
				return l;
		}
		
		return null;
	}
	
	/**
	 * Generate the list of all possible locations on the board 
	 * and their direct neighbors (location through their doorways). 
	 * @return
	 */
	public static List<Location> generateLocations() {
		List<Location> locations = new ArrayList<Location>();
		
		for(AREA area : AREA.values()) {
			Location l = new Location(area);
			
			switch (area) {
			case Study:
				l.neighbors.add(AREA.HW_SH);
				l.neighbors.add(AREA.HW_SL);
				l.neighbors.add(AREA.Kitchen);
				l.isRoom = true;
				break;
			case Hall:
				l.neighbors.add(AREA.HW_SH);
				l.neighbors.add(AREA.HW_HL);
				l.neighbors.add(AREA.HW_HB);
				l.isRoom = true;
				break;
			case Lounge:
				l.neighbors.add(AREA.HW_HL);
				l.neighbors.add(AREA.HW_LD);
				l.neighbors.add(AREA.Conservatory);
				l.isRoom = true;
				break;
			case Library:
				l.neighbors.add(AREA.HW_SL);
				l.neighbors.add(AREA.HW_LB);
				l.neighbors.add(AREA.HW_LC);
				l.isRoom = true;
				break;
			case BilliardRoom:
				l.neighbors.add(AREA.HW_HB);
				l.neighbors.add(AREA.HW_LB);
				l.neighbors.add(AREA.HW_BD);
				l.neighbors.add(AREA.HW_BB);
				l.isRoom = true;
				break;
			case DiningRoom:
				l.neighbors.add(AREA.HW_LD);
				l.neighbors.add(AREA.HW_BD);
				l.neighbors.add(AREA.HW_DK);
				l.isRoom = true;
				break;
			case Conservatory:
				l.neighbors.add(AREA.HW_LC);
				l.neighbors.add(AREA.HW_CB);
				l.neighbors.add(AREA.Lounge);
				l.isRoom = true;
				break;
			case Ballroom:
				l.neighbors.add(AREA.HW_BB);
				l.neighbors.add(AREA.HW_CB);
				l.neighbors.add(AREA.HW_BK);
				l.isRoom = true;
				break;
			case Kitchen:
				l.neighbors.add(AREA.HW_DK);
				l.neighbors.add(AREA.HW_BK);
				l.neighbors.add(AREA.Study);
				l.isRoom = true;
				break;
			case HW_SH:
				l.neighbors.add(AREA.Study);
				l.neighbors.add(AREA.Hall);
				break;
			case HW_HL:
				l.neighbors.add(AREA.Hall);
				l.neighbors.add(AREA.Lounge);
				break;
			case HW_SL:
				l.neighbors.add(AREA.Study);
				l.neighbors.add(AREA.Library);
				break;
			case HW_HB:
				l.neighbors.add(AREA.Hall);
				l.neighbors.add(AREA.BilliardRoom);
				break;
			case HW_LD:
				l.neighbors.add(AREA.Lounge);
				l.neighbors.add(AREA.DiningRoom);
				break;
			case HW_LB:
				l.neighbors.add(AREA.Library);
				l.neighbors.add(AREA.BilliardRoom);
				break;
			case HW_BD:
				l.neighbors.add(AREA.BilliardRoom);
				l.neighbors.add(AREA.DiningRoom);
				break;
			case HW_LC:
				l.neighbors.add(AREA.Library);
				l.neighbors.add(AREA.Conservatory);
				break;
			case HW_BB:
				l.neighbors.add(AREA.BilliardRoom);
				l.neighbors.add(AREA.Ballroom);
				break;
			case HW_DK:
				l.neighbors.add(AREA.DiningRoom);
				l.neighbors.add(AREA.Kitchen);
				break;
			case HW_CB:
				l.neighbors.add(AREA.Conservatory);
				l.neighbors.add(AREA.Ballroom);
				break;
			case HW_BK:
				l.neighbors.add(AREA.Ballroom);
				l.neighbors.add(AREA.Kitchen);
				break;
			}
			locations.add(l);
		}
		
		return locations;
	}
	
	public Location getDefaultLocation(Board.CHARACTER character) {
		AREA defaultArea = null;
		
		switch (character) {
		case ColMustard:
			defaultArea = AREA.HW_LD;
			break;
		case MrGreen:
			defaultArea = AREA.HW_CB;
			break;
		case MrsPeacock:
			defaultArea = AREA.HW_LC;
			break;
		case MrsWhite:
			defaultArea = AREA.HW_BK;
			break;
		case MsScarlet:
			defaultArea = AREA.HW_HL;
			break;
		case ProfPlum:
			defaultArea = AREA.HW_SL;
			break;
		}
		
		Location defaultLocation = getLocation(defaultArea);
		return defaultLocation;
	}

}
