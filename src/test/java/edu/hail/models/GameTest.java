package edu.hail.models;

import org.junit.Test;
import edu.hail.models.Board.CHARACTER;
import edu.hail.models.Board.Location;
import edu.hail.models.Game;
import org.junit.Assert;

public class GameTest {

	@Test
	public void hasSolution() {
		Game g = null;
		
		g = new Game();
		g.start();
		
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
		g.start();
		
		String solutionRoom = g.getRoom().name();
		String solutionWeapon = g.getWeapon().name();
		String solutionSuspect = g.getSuspect().name();
		
		Assert.assertTrue("The game solution must be accurately guessed.", g.solve(solutionRoom, solutionWeapon, solutionSuspect));
	}
	
	@Test
	public void verifyData() {
		int roomCount = 0, hallCount = 0;
		for (Board.Location loc : Board.generateLocations()) {
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
	
	@Test
	public void cardDealingTest() {
		Game g = null;
		
		g = new Game();
		
		// Add fake players
		for (int i = 0; i < 5; i++) {
			User u = new User("test_" + i, null);
			u.character = CHARACTER.values()[i];
			g.addPlayer(u);
		}
		g.start();
		
		int cardCount = 0;
		for(User u : g.players) {
			// Does each player have cards
			Assert.assertTrue(u.cards.size() > 0);

			// Ensure no solution cards are in anyone's hands
			Assert.assertFalse(u.cards.contains(g.getRoom().name()));
			Assert.assertFalse(u.cards.contains(g.getWeapon().name()));
			Assert.assertFalse(u.cards.contains(g.getSuspect().name()));
			
			// TODO: Ensure no one has the same card as another
			
			// tally total cards dealt
			cardCount += u.cards.size();			
		}
		
		Assert.assertEquals(cardCount, g.getDeck().size() - 3);
	}
	
	@Test
	public void LocationRetrievalTest() {
		Game g = null;
		
		g = new Game();
		
		// Add fake players
		for (int i = 0; i < 5; i++) {
			User u = new User("test_" + i, null);
			u.character = CHARACTER.values()[i];
			g.addPlayer(u);
		}
		g.start();
		
		// Verify that ballroom has the expected neighbors
		Location ballroom = g.board.getLocation(Board.AREA.Ballroom);
		Assert.assertTrue(ballroom.neighbors.size() == 3);
		Assert.assertTrue(ballroom.neighbors.contains(Board.AREA.HW_CB));
		Assert.assertTrue(ballroom.neighbors.contains(Board.AREA.HW_BB));
		Assert.assertTrue(ballroom.neighbors.contains(Board.AREA.HW_BK));
		
		// Verify each player's location can be retrieved
		for(User u : g.players) {
			Location idk = g.board.getLocation(u.character);
			Assert.assertNotNull(idk);
			Assert.assertTrue(idk.neighbors.size() >= 2);
		}
	}
}
