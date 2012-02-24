package controllers.services;

import static org.junit.Assert.assertThat;

import java.util.List;

import models.Leaderboard;
import models.Score;
import models.User;

import org.hamcrest.core.IsEqual;
import org.joda.time.DateTime;
import org.junit.Test;

public class LeaderboardServiceTest {

	static class Result {

		int candidateScope = 1;
		int replacedScores = -1;
		boolean todayScoreExists = false;

	}

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

		Result result = calculateScoreScopeAndStuff(score, scores);

		if (result.replacedScores != -1)
			deleteOldScores(scores, result.replacedScores);

		if (result.replacedScores != -1 || result.todayScoreExists == false) {
			score.scope = result.candidateScope;
			score.save();
		}
	}

	static private void deleteOldScores(Score[] scores, int from) {
		for (int i = from; i < scores.length; i++) {
			scores[i].delete();
		}
	}

	public static Result calculateScoreScopeAndStuff(Score score, Score[] scores) {
		Result result = new Result();

		for (int i = 0; i < scores.length; i++) {
			Score oldScore = scores[i];
			result.todayScoreExists = result.todayScoreExists || oldScore.day == score.day;
			if (matchesInScope(oldScore, oldScore)) {
				if (score.score > oldScore.score) {
					score.scope = oldScore.scope;
					result.replacedScores = i;
					break;
				} else {
					result.candidateScope++;
				}
			}
		}
		return result;
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

	private Score score(int year, int month, int week, int day, int scope, long points) {
		Score score = new Score();
		score.score = points;
		score.scope = scope;
		score.year = year;
		score.month = month;
		score.week = week;
		score.day = day;
		return score;
	}

	@Test
	public void shouldBeBestScoreIfNoOtherScore() {
		Score[] scores = new Score[] {};
		Score score = score(1, 1, 1, 1, -100, 500);
		Result result = calculateScoreScopeAndStuff(score, scores);
		assertThat(result.candidateScope, IsEqual.equalTo(1));
		assertThat(result.replacedScores, IsEqual.equalTo(-1));
	}

	@Test
	public void shouldBeBestIfBetterThanPreviousBest() {
		Score[] scores = new Score[] { score(1, 1, 1, 1, 1, 250) };
		Score score = score(1, 1, 1, 1, -100, 500);
		Result result = calculateScoreScopeAndStuff(score, scores);
		assertThat(result.candidateScope, IsEqual.equalTo(1));
		assertThat(result.replacedScores, IsEqual.equalTo(0));
	}

	@Test
	public void shouldNotBeConsideredIfNotBetterThanPreviousForSameDate() {
		Score[] scores = new Score[] { score(1, 1, 1, 1, 1, 500) };
		Score score = score(1, 1, 1, 1, -100, 250);
		Result result = calculateScoreScopeAndStuff(score, scores);
		assertThat(result.replacedScores, IsEqual.equalTo(-1));
	}

	@Test
	public void shouldBeConsideredForBestOfMonth() {
		Score[] scores = new Score[] { score(1, 1, 1, 1, 1, 500), score(1, 1, 1, 1, 2, 250) };
		Score score = score(1, 1, 1, 1, -100, 350);
		Result result = calculateScoreScopeAndStuff(score, scores);
		assertThat(result.candidateScope, IsEqual.equalTo(2));
		assertThat(result.replacedScores, IsEqual.equalTo(1));
	}

	@Test
	public void shouldNotBeConsideredAsBestOfMonth() {
		Score[] scores = new Score[] { score(1, 1, 1, 1, 1, 500), score(1, 1, 1, 1, 2, 450) };
		Score score = score(1, 1, 1, 1, -100, 350);
		Result result = calculateScoreScopeAndStuff(score, scores);
		assertThat(result.replacedScores, IsEqual.equalTo(-1));
	}

	@Test
	public void shouldBeBestOfTodayButNotYesterday() {
		Score[] scores = new Score[] { //
				score(1, 1, 1, 1, 1, 500), //
				score(1, 1, 1, 2, 2, 450), //
				score(1, 1, 1, 3, 3, 350), //
				score(1, 1, 1, 4, 4, 250) //
		};
		Score score = score(1, 1, 1, 5, -100, 150);
		Result result = calculateScoreScopeAndStuff(score, scores);
		assertThat(result.todayScoreExists, IsEqual.equalTo(false));
		assertThat(result.candidateScope, IsEqual.equalTo(4));
		assertThat(result.replacedScores, IsEqual.equalTo(-1));
	}

}
