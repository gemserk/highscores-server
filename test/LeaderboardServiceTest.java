import java.util.List;

import models.Leaderboard;
import models.Score;
import models.User;

import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import controllers.services.LeaderboardService;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;
import utils.Range;
import utils.URLBuilder;

public class LeaderboardServiceTest extends FunctionalTest {

	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("testdata.yaml");
	}

	@Test
	public void submitASCore() {

		User user = User.find("byName", "user").first();
		Leaderboard leaderboard = Leaderboard.find("byName", "leaderboard").first();
		
		LeaderboardService.submitScore(leaderboard, user, 999l, new DateTime());
		
		List<Score> scores = LeaderboardService.getScores(leaderboard, Range.All, new DateTime(), 1, 10);
		
		Score score = scores.get(0);
		assertThat(score.score, CoreMatchers.equalTo(999l));
		assertThat(score.user.id, CoreMatchers.equalTo(user.id));
		
	}

}
