package controllers.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import models.Leaderboard;
import models.Score;
import models.User;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import utils.Range;
import controllers.services.LeaderboardService.Result;

public class LeaderboardServiceTest {

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
	
	private Score score(int year, int month, int week, int day) {
		Score score = new Score();
		score.year = year;
		score.month = month;
		score.week = week;
		score.day = day;
		return score;
	}

	private Score score(int year, int month, int week, int day, Range range, long points) {
		return score(year, month, week, day, range.scope, points);
	}

	@Test
	public void shouldBeBestScoreIfNoOtherScore() {
		Score[] scores = new Score[] {};
		Score score = score(1, 1, 1, 1, 1, 500);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(result.todayScoreExists, equalTo(false));
		assertThat(score.scope, equalTo(1));
		assertThat(result.replacedScores, equalTo(-1));
	}

	@Test
	public void shouldBeBestIfBetterThanPreviousBest() {
		Score[] scores = new Score[] { score(1, 1, 1, 1, 1, 250) };
		Score score = score(1, 1, 1, 1, 1, 500);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(score.scope, equalTo(1));
		assertThat(result.replacedScores, equalTo(0));
	}

	@Test
	public void shouldNotBeConsideredIfNotBetterThanPreviousForSameDate() {
		Score[] scores = new Score[] { score(1, 1, 1, 1, 1, 500) };
		Score score = score(1, 1, 1, 1, 1, 250);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(result.replacedScores, equalTo(-1));
	}

	@Test
	public void shouldBeConsideredForBestOfMonth() {
		Score[] scores = new Score[] { //
		score(1, 1, 1, 1, 1, 500), //
				score(1, 1, 1, 1, 2, 250) //
		};
		Score score = score(1, 1, 1, 1, 1, 350);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(score.scope, equalTo(2));
		assertThat(result.replacedScores, equalTo(1));
	}

	@Test
	public void shouldNotBeConsideredAsBestOfMonth() {
		Score[] scores = new Score[] { score(1, 1, 1, 1, 1, 500), score(1, 1, 1, 1, 2, 450) };
		Score score = score(1, 1, 1, 1, 1, 350);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(result.replacedScores, equalTo(-1));
	}

	@Test
	public void shouldBeBestOfTodayButNotYesterday() {
		Score[] scores = new Score[] { score(1, 1, 1, 1, 1, 500), //
				score(1, 1, 1, 2, 2, 450), //
				score(1, 1, 1, 3, 3, 350), //
				score(1, 1, 1, 4, 4, 250) //
		};
		Score score = score(1, 1, 1, 5, 1, 150);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(result.todayScoreExists, equalTo(false));
		assertThat(score.scope, equalTo(4));
		assertThat(result.replacedScores, equalTo(-1));
	}

	@Test
	public void shouldBeBestOfTodayWhenSameDateThanAll() {
		Score[] scores = new Score[] { //
		score(1, 1, 1, 1, 1, 500), //
		};
		Score score = score(1, 1, 1, 2, 1, 400);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(result.todayScoreExists, equalTo(false));
		assertThat(score.scope, equalTo(Range.Day.scope));
		assertThat(result.replacedScores, equalTo(-1));
	}

	@Test
	public void shouldBeBestOfWeekWhenSameDateThanAll() {
		Score[] scores = new Score[] { //
		score(1, 1, 1, 1, 1, 500), //
		};
		Score score = score(1, 1, 2, 8, 1, 400);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(result.todayScoreExists, equalTo(false));
		assertThat(score.scope, equalTo(Range.Week.scope));
		assertThat(result.replacedScores, equalTo(-1));
	}

	@Test
	public void shouldBeBestOfMonthWhenSameDateThanAll() {
		Score[] scores = new Score[] { //
		score(1, 1, 1, 1, 1, 500), //
		};
		Score score = score(1, 2, 5, 50, 1, 400);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(result.todayScoreExists, equalTo(false));
		assertThat(score.scope, equalTo(Range.Month.scope));
		assertThat(result.replacedScores, equalTo(-1));
	}

	@Test
	public void shouldReplaceBestOfSecondWeek() {
		Score[] scores = new Score[] { //
		score(1, 1, 1, 1, 1, 500), //
				score(1, 1, 2, 8, 3, 400), //
		};
		Score score = score(1, 1, 2, 8, 1, 450);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(result.todayScoreExists, equalTo(true));
		assertThat(score.scope, equalTo(3));
		assertThat(result.replacedScores, equalTo(1));
	}

	@Test
	public void testCaseSameWeekSameDayLessScore() {
		Score[] scores = new Score[] { //
		score(1, 1, 1, 1, 1, 500), //
				score(1, 1, 2, 8, 3, 400), //
		};
		Score score = score(1, 1, 2, 8, 1, 350);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(result.todayScoreExists, equalTo(true));
		assertThat(result.replacedScores, equalTo(-1));
	}

	@Test
	public void testCaseSameWeekDifferentDayLessScore() {
		Score[] scores = new Score[] { //
		score(1, 1, 1, 1, 1, 500), //
				score(1, 1, 2, 8, 3, 400), //
		};
		Score score = score(1, 1, 2, 9, 1, 350);
		Result result = LeaderboardService.calculateScoreScopeAndStuff(score, scores);
		assertThat(result.todayScoreExists, equalTo(false));
		assertThat(score.scope, equalTo(4));
		assertThat(result.replacedScores, equalTo(-1));
	}

	@Test
	public void testCalculateCandidateScope1() {
		assertThat(LeaderboardService.calculateCandidateScopeForWorseScore( //
				score(1, 1, 1, 1),  //
				score(2, 12, 12, 12)), // 
				equalTo(Range.Month.scope));
		
		assertThat(LeaderboardService.calculateCandidateScopeForWorseScore( //
				score(1, 1, 1, 1),  //
				score(1, 1, 1, 2)), // 
				equalTo(Range.Day.scope));
		
		assertThat(LeaderboardService.calculateCandidateScopeForWorseScore( //
				score(1, 1, 1, 1),  //
				score(1, 1, 2, 8)), // 
				equalTo(Range.Week.scope));
		
		assertThat(LeaderboardService.calculateCandidateScopeForWorseScore( //
				score(1, 1, 1, 1),  //
				score(1, 2, 4, 32)), // 
				equalTo(Range.Month.scope));
	}

}
