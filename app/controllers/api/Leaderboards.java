package controllers.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;

import datatransfer.ScoreDTO;

import models.Game;
import models.Leaderboard;
import models.Score;
import models.User;
import play.Play;
import play.db.jpa.JPABase;
import play.mvc.Controller;
import utils.Range;

public class Leaderboards extends Controller {

	private static final String scoresByAllTime = "select s from Score s where s.leaderboard = ? and s.scope <= ? order by s.score desc";
	private static final String scoresByDay = "select s from Score s where s.leaderboard = ? and s.scope <= ? and s.year = ? and s.day = ? order by s.score desc";
	private static final String scoresByWeek = "select s from Score s where s.leaderboard = ? and s.scope <= ? and s.year = ? and s.week = ? order by s.score desc";
	private static final String scoresByMonth = "select s from Score s where s.leaderboard = ? and s.scope <= ? and s.year = ? and s.month = ? order by s.score desc";

	static public void score() {
		// Long leaderboardId = params.get("leaderboardId", Long.class);
		String apiKey = params.get("apiKey");
		Game game = Game.find("byApiKey", apiKey).first();
		if (game == null)
			error("No game matching apikey was found");

		String leaderboardName = params.get("leaderboard");
		if (leaderboardName == null)
			error("You need to provide the leaderboard name");

		Leaderboard leaderboard = Leaderboard.find("byGameAndName", game, leaderboardName).first();

		if (leaderboard == null)
			error("Leaderboard (" + leaderboardName + ") doesn't match game (" + game.name + ")");

		String username = params.get("user");
		if (username == null)
			error("You need to provide the username");

		User user = User.find("byUsername", username).first();
		if (!user.privatekey.equals(params.get("privatekey")))
			error("User authentication failed");

		Long scoreValue = params.get("score", Long.class);

		DateTime dateTime = new DateTime();
		String overridendayofyear = params.get("dayofyear");
		if (Play.runingInTestMode() && overridendayofyear != null) {
			System.out.println("Overriding Date to " + overridendayofyear);
			dateTime = dateTime.dayOfYear().setCopy(Integer.parseInt(overridendayofyear));
		}

		int year = dateTime.getYear();
		int month = dateTime.getMonthOfYear();
		int week = dateTime.getWeekOfWeekyear();
		int day = dateTime.getDayOfYear();

		Score score = new Score();
		score.leaderboard = leaderboard;
		score.score = scoreValue;
		score.user = user;

		score.year = year;
		score.month = month;
		score.week = week;
		score.day = day;

		List<Score> scoresList = Score.find("user = ? order by scope", user).fetch();
		Score[] scores = scoresList.toArray(new Score[scoresList.size()]);
		int candidateScope = 1;

		int replacedScores = -1;
		boolean todayScoreExists = false;

		for (int i = 0; i < scores.length; i++) {
			Score oldScore = scores[i];
			todayScoreExists = todayScoreExists || oldScore.day == day;
			if (matchesInScope(oldScore, oldScore)) {
				if (score.score > oldScore.score) {
					score.scope = oldScore.scope;
					replacedScores = i;
					break;
				} else {
					candidateScope++;
				}
			}
		}

		if (replacedScores != -1)
			deleteOldScores(scores, replacedScores);

		if (replacedScores != -1 || todayScoreExists == false) {
			score.scope = candidateScope;
			score.save();
		}
		renderText("Score submitted succesfully");
	}

	private static boolean matchesInScope(Score oldScore, Score score) {
		switch (oldScore.scope) {
		case 1:
			return true;
		case 2:
			return (score.year == oldScore.year && score.month == oldScore.month);
		case 3:
			return (score.year == oldScore.year && score.month == oldScore.month && score.week == oldScore.week);
		case 4:
			return (score.year == oldScore.year && score.month == oldScore.month && score.week == oldScore.week && score.day == oldScore.day);
		default:
			return false;
		}

	}

	static private void deleteOldScores(Score[] scores, int from) {
		for (int i = from; i < scores.length; i++) {
			scores[i].delete();
		}
	}

	public static void scores() {
		
		System.out.println(params.allSimple());
		
		String apiKey = params.get("apiKey");
		Game game = Game.find("byApiKey", apiKey).first();
		if (game == null)
			error("No game matching apikey was found");

		String leaderboardName = params.get("leaderboard");
		if (leaderboardName == null)
			error("You need to provide the leaderboard name");

		Leaderboard leaderboard = Leaderboard.find("byGameAndName", game, leaderboardName).first();

		if (leaderboard == null)
			error("Leaderboard (" + leaderboardName + ") doesn't match game (" + game.name + ")");

		String rangeKey = params.get("range");

		Range range = Range.getByKey(rangeKey);
		if (range == null)
			range = Range.All;

		int scope = range.scope;

		DateTime dateTime = new DateTime();
		String overridendayofyear = params.get("dayofyear");
		if (Play.runingInTestMode() && overridendayofyear != null) {
			System.out.println("Overriding Date to " + overridendayofyear);
			dateTime = dateTime.dayOfYear().setCopy(Integer.parseInt(overridendayofyear));
		}

		int year = dateTime.getYear();
		int month = dateTime.getMonthOfYear();
		int week = dateTime.getWeekOfWeekyear();
		int day = dateTime.getDayOfYear();

		List<Score> scores = new ArrayList<Score>();
		switch (range) {
		case All:
			scores = Leaderboard.find(scoresByAllTime, leaderboard, scope).fetch();
			break;
		case Month:
			scores = Leaderboard.find(scoresByMonth, leaderboard, scope, year, month).fetch();
			break;
		case Week:
			scores = Leaderboard.find(scoresByWeek, leaderboard, scope, year, week).fetch();
			break;
		case Day:
			scores = Leaderboard.find(scoresByDay, leaderboard, scope, year, day).fetch();
			break;
		}

		List<ScoreDTO> scoreDTOs = ScoreDTO.convert(scores);
		renderJSON(scoreDTOs);

	}


}
