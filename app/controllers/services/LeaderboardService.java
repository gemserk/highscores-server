package controllers.services;

import java.util.List;

import models.Leaderboard;
import models.Score;
import models.User;

import org.joda.time.DateTime;

import play.db.jpa.GenericModel.JPAQuery;
import utils.Range;

import controllers.api.Leaderboards;

public class LeaderboardService {

	private static final String scoresByAllTime = "select s from Score s inner join fetch s.user where s.leaderboard = ? and s.scope <= ? order by s.score desc";
	private static final String scoresByMonth = "select s from Score s inner join fetch s.user where s.leaderboard = ? and s.scope <= ? and s.year = ? and s.month = ? order by s.score desc";
	private static final String scoresByWeek = "select s from Score s inner join fetch s.user where s.leaderboard = ? and s.scope <= ? and s.year = ? and s.week = ? order by s.score desc";
	private static final String scoresByDay = "select s from Score s inner join fetch s.user where s.leaderboard = ? and s.scope <= ? and s.year = ? and s.day = ? order by s.score desc";
	
	public static void submitScore(Leaderboard leaderboard, User user, Long scoreValue, DateTime dateTime) {
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
	
	
	public static List<Score> getScores(Leaderboard leaderboard, Range range, DateTime dateTime, int page, int pageSize) {
		int scope = range.scope;
		int year = dateTime.getYear();
		int month = dateTime.getMonthOfYear();
		int week = dateTime.getWeekOfWeekyear();
		int day = dateTime.getDayOfYear();

		JPAQuery query = null;
		
		switch (range) {
		case All:
			query = Leaderboard.find(scoresByAllTime, leaderboard, scope);
			break;
		case Month:
			query = Leaderboard.find(scoresByMonth, leaderboard, scope, year, month);
			break;
		case Week:
			query = Leaderboard.find(scoresByWeek, leaderboard, scope, year, week);
			break;
		case Day:
			query = Leaderboard.find(scoresByDay, leaderboard, scope, year, day);
			break;
		}
		
		
		
		List<Score> scores = query.fetch(page,pageSize);
		return scores;
	}

}
