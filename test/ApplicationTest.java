import java.lang.reflect.Type;
import java.util.List;

import models.Score;
import models.User;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;
import utils.Range;
import utils.URLBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import datatransfer.ScoreDTO;
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
				.addQueryParameter("privatekey", user.privatekey)//
				.addQueryParameter("user", user.username)//
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
				.addQueryParameter("privatekey", user.privatekey)//
				.addQueryParameter("user", user.username)//
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
				.addQueryParameter("privatekey", user.privatekey)//
				.addQueryParameter("user", user.username)//
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
				.addQueryParameter("privatekey", user.privatekey)//
				.addQueryParameter("user", user.username)//
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
				.addQueryParameter("privatekey", user.privatekey)//
				.addQueryParameter("user", user.username)//
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

	// two scores in different days but same month, the first one is the best score of all time, the second one is lower, and you can find it when you query by week or day.
	@Test
	public void submitTwoScoresDifferentDay() {

		User user = User.find("byUsername", "user").first();

		long scoreValue = 100;

		String url = new URLBuilder("/leaderboards/score")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("privatekey", user.privatekey)//
				.addQueryParameter("user", user.username)//
				.addQueryParameter("score", Long.toString(scoreValue))//
				.addQueryParameter("dayofyear", "1")//
				.build();
		Response response = GET(url);

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertContentEquals("Score submitted succesfully", response);

		long scoreValue2 = 50;

		url = new URLBuilder("/leaderboards/score")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("privatekey", user.privatekey)//
				.addQueryParameter("user", user.username)//
				.addQueryParameter("score", Long.toString(scoreValue2))//
				.addQueryParameter("dayofyear", "20")//
				.build();
		response = GET(url);

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertContentEquals("Score submitted succesfully", response);

		url = new URLBuilder("/leaderboards/scores")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("range", Range.All.key)//
				.addQueryParameter("dayofyear", "20")//
				.build();
		response = GET(url);

		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset(play.Play.defaultWebEncoding, response);

		String content = getContent(response);
		Gson gson = new Gson();
		Type collectionType = new TypeToken<List<ScoreDTO>>() {
		}.getType();
		
		List<ScoreDTO> scores = gson.fromJson(content, collectionType);

		assertEquals(1, scores.size());
		assertEquals(scoreValue, scores.get(0).score);

		url = new URLBuilder("/leaderboards/scores")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("range", Range.Day.key)//
				.addQueryParameter("dayofyear", "20")//
				.build();
		response = GET(url);

		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset(play.Play.defaultWebEncoding, response);

		content = getContent(response);
		gson = new Gson();

		collectionType = new TypeToken<List<ScoreDTO>>() {
		}.getType();

		scores = gson.fromJson(content, collectionType);

		assertEquals(1, scores.size());
		assertEquals(scoreValue2, scores.get(0).score);
	}
	

	@Test
	public void submitTwoScoresForDifferentUsers() {

		User user = User.find("byUsername", "user").first();
		User user2 = User.find("byUsername", "user2").first();

		long scoreValue = 100;

		String url = new URLBuilder("/leaderboards/score")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("privatekey", user.privatekey)//
				.addQueryParameter("user", user.username)//
				.addQueryParameter("score", Long.toString(scoreValue))//
				.addQueryParameter("dayofyear", "1")//
				.build();
		Response response = GET(url);

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertContentEquals("Score submitted succesfully", response);

		long scoreValue2 = 50;

		url = new URLBuilder("/leaderboards/score")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("privatekey", user2.privatekey)//
				.addQueryParameter("user", user2.username)//
				.addQueryParameter("score", Long.toString(scoreValue2))//
				.addQueryParameter("dayofyear", "1")//
				.build();
		response = GET(url);

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertContentEquals("Score submitted succesfully", response);

		url = new URLBuilder("/leaderboards/scores")//
				.addQueryParameter("leaderboard", "leaderboard")//
				.addQueryParameter("apiKey", "apikey")//
				.addQueryParameter("range", Range.All.key)//
				.addQueryParameter("dayofyear", "20")//
				.build();
		response = GET(url);

		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset(play.Play.defaultWebEncoding, response);

		String content = getContent(response);
		Gson gson = new Gson();
		Type collectionType = new TypeToken<List<ScoreDTO>>() {
		}.getType();
		
		List<ScoreDTO> scores = gson.fromJson(content, collectionType);

		assertEquals(2, scores.size());
		assertEquals(scoreValue, scores.get(0).score);
		assertEquals(user.name, scores.get(0).name);
		assertEquals(scoreValue2, scores.get(1).score);
		assertEquals(user2.name, scores.get(1).name);
		
		
	}

}