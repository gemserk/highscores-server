import java.util.List;

import models.Score;
import models.User;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.IsCollectionContaining;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;
import utils.URLBuilder;

import com.google.gson.Gson;

import datatransfer.UserDTO;

public class ApplicationTest extends FunctionalTest {

	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("testdata.yaml");
	}

	@Test
	public void createAGuestUser() {

		String url = new URLBuilder("/users/createGuest").build();
		Response response = GET(url);

		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset(play.Play.defaultWebEncoding, response);

		String content = getContent(response);
		Gson gson = new Gson();
		UserDTO userDTO = gson.fromJson(content, UserDTO.class);

		assertTrue(userDTO.username.startsWith("Player"));
	}

	@Test
	public void submitAScore() {

		// /leaderboards/score?leaderboardId=1&apiKey=apikey&score=100&authToken=authToken&user=1&authToken=authToken

		User user = User.find("byUsername", "user").first();

		long scoreValue = 100;

		String url = new URLBuilder("/leaderboards/score")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("authToken", "authToken")//
				.addQueryParameter("user", Long.toString(user.id))//
				.addQueryParameter("score", Long.toString(scoreValue))//
				.build();
		Response response = GET(url);

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertContentEquals("Score submitted succesfully", response);

		List<Score> scores = Score.find("byUser", user).fetch();
		assertThat(scores.size(), CoreMatchers.equalTo(1));
		Score score = scores.get(0);
		assertThat(score.score, CoreMatchers.equalTo(scoreValue));
		assertThat(score.scope, CoreMatchers.equalTo(1));
	}

	@Test
	public void submitAHigherScoreOnTheSameDayWhenTheFirstOneWasBestOfAllTime() {

		// /leaderboards/score?leaderboardId=1&apiKey=apikey&score=100&authToken=authToken&user=1&authToken=authToken

		User user = User.find("byUsername", "user").first();

		long scoreValue = 100;

		String url = new URLBuilder("/leaderboards/score")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("authToken", "authToken")//
				.addQueryParameter("user", Long.toString(user.id))//
				.addQueryParameter("score", Long.toString(scoreValue))//
				.build();
		Response response = GET(url);

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertContentEquals("Score submitted succesfully", response);

		List<Score> scores = Score.find("byUser", user).fetch();
		assertThat(scores.size(), CoreMatchers.equalTo(1));
		Score score = scores.get(0);
		assertThat(score.score, CoreMatchers.equalTo(scoreValue));
		assertThat(score.scope, CoreMatchers.equalTo(1));

		long scoreValue2 = 150;

		url = new URLBuilder("/leaderboards/score")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("authToken", "authToken")//
				.addQueryParameter("user", Long.toString(user.id))//
				.addQueryParameter("score", Long.toString(scoreValue2))//
				.build();
		response = GET(url);

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertContentEquals("Score submitted succesfully", response);

		scores = Score.find("byUser", user).fetch();
		assertThat(scores.size(), CoreMatchers.equalTo(1));
		score = scores.get(0);
		assertThat(score.score, CoreMatchers.equalTo(scoreValue2));
		assertThat(score.scope, CoreMatchers.equalTo(1));
	}
	
	
	@Test
	public void submitALowerScoreOnTheSameDayWhenTheFirstOneWasBestOfAllTime() {

		// /leaderboards/score?leaderboardId=1&apiKey=apikey&score=100&authToken=authToken&user=1&authToken=authToken

		User user = User.find("byUsername", "user").first();

		long scoreValue = 100;

		String url = new URLBuilder("/leaderboards/score")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("authToken", "authToken")//
				.addQueryParameter("user", Long.toString(user.id))//
				.addQueryParameter("score", Long.toString(scoreValue))//
				.build();
		Response response = GET(url);

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertContentEquals("Score submitted succesfully", response);

		List<Score> scores = Score.find("byUser", user).fetch();
		assertThat(scores.size(), CoreMatchers.equalTo(1));
		Score score = scores.get(0);
		assertThat(score.score, CoreMatchers.equalTo(scoreValue));

		long scoreValue2 = 50;

		url = new URLBuilder("/leaderboards/score")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("authToken", "authToken")//
				.addQueryParameter("user", Long.toString(user.id))//
				.addQueryParameter("score", Long.toString(scoreValue2))//
				.build();
		response = GET(url);

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertContentEquals("Score submitted succesfully", response);

		scores = Score.find("byUser", user).fetch();
		assertThat(scores.size(), CoreMatchers.equalTo(1));
		score = scores.get(0);
		assertThat(score.score, CoreMatchers.equalTo(scoreValue));
	}

}