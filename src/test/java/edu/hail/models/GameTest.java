package edu.hail.models;

import org.junit.Test;

import edu.hail.models.Game;
import org.junit.Assert;

public class GameTest {

	@Test
	public void hasSolution() {
		Game g = null;
		
		g = new Game();
		
		Assert.assertNotNull("Solution must have a room.", g.getRoom());
		System.out.println("Room: " + g.getRoom());
		Assert.assertNotNull("Solution must have a weapon.", g.getWeapon());
		System.out.println("Weapon: " + g.getWeapon());
		Assert.assertNotNull("Solution must have a suspect.", g.getSuspect());
		System.out.println("Suspect: " + g.getSuspect());
	}
	
	@Test
	public void solveTest() {
		Game g = null;
		
		g = new Game();
		
		String solutionRoom = g.getRoom().name();
		String solutionWeapon = g.getWeapon().name();
		String solutionSuspect = g.getSuspect().name();
		
		Assert.assertTrue("The game solution must be accurately guessed.", g.solve(solutionRoom, solutionWeapon, solutionSuspect));
	}
	
	@Test
	public void verifyData() {
		int roomCount = 0, hallCount = 0;
		for (Board.Location loc : Board.getLocations()) {
			if (loc.isRoom) {
				roomCount++;
				Assert.assertTrue("Room must have at least 3 neighbors.", loc.neighbors.size() >= 3);
			}
			else {
				hallCount++;
				Assert.assertTrue("Hallway must have exactly 2 neighbors.", loc.neighbors.size() == 2);
			}
		}
		Assert.assertEquals(9, roomCount);
		Assert.assertEquals(12, hallCount);
		
		
	}
}
