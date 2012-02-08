package controllers.test;

import java.util.Random;

import org.joda.time.DateTime;

import controllers.services.LeaderboardService;
import models.Game;
import models.Leaderboard;
import models.User;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.test.Fixtures;

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
		User[] users = User.findAll().toArray(new User[]{});
		
		Leaderboard leaderboard = (Leaderboard) Leaderboard.findAll().get(0);
		Game game = leaderboard.game;
		DateTime dateTime = new DateTime();
		
		Random random = new Random();
		for(int i = 0; i<100; i++){
			User user = users[random.nextInt(users.length)];
			long scoreValue = random.nextInt(1000);
			if(random.nextFloat()<0.2f)
				dateTime = dateTime.dayOfYear().addToCopy(1);
			
			LeaderboardService.submitScore(leaderboard, user, scoreValue, dateTime);
		}
		
	}
}
