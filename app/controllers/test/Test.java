package controllers.test;

import java.util.Random;

import models.Game;
import models.Leaderboard;
import models.User;

import org.joda.time.DateTime;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.test.Fixtures;
import controllers.services.LeaderboardService;

public class Test extends Controller {

	@Before
	static void onlyRunInTest() {
		if (!Play.runingInTestMode())
			error(403, "Only available in test mode");
	}

	static public void init() {
		Fixtures.loadModels("test/testdata.yaml");
		renderText("Load Successful");
	}

	static public void delete() {
		Fixtures.deleteDatabase();
		renderText("DeleteLoad Successful");
	}

	static public void reset() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("test/testdata.yaml");
		renderText("ResetData Successful");
	}

	static public void generateLotsOfScores(){
		long timeStart = System.currentTimeMillis();
		Integer quantity = params.get("quantity",Integer.class);
		if(quantity==null)
			quantity = 100;
		
		Float chance = params.get("chance",Float.class);
		if(chance==null)
			chance = 0.2f;
		
		User[] users = User.findAll().toArray(new User[]{});
		
		Leaderboard leaderboard = (Leaderboard) Leaderboard.findAll().get(0);
		Game game = leaderboard.game;
		DateTime dateTime = new DateTime();
		
		Random random = new Random();
		for(int i = 0; i<quantity; i++){
			User user = users[random.nextInt(users.length)];
			long scoreValue = random.nextInt(1000);
			if(random.nextFloat()<chance)
				dateTime = dateTime.dayOfYear().addToCopy(1);
			
			LeaderboardService.submitScore(leaderboard, user, scoreValue, dateTime);
			if(i%100==0)
				System.out.println("Submitting score: " + i);
		}
		
		long duration = System.currentTimeMillis() - timeStart;
		
		renderText("Time to create " + quantity + " scores was: " + duration + " milliseconds");
		
	}
}
