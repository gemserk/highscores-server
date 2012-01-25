package controllers.api;

import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;

import models.Game;
import models.Leaderboard;
import models.Score;
import models.User;
import play.db.jpa.JPABase;
import play.mvc.Controller;

public class Leaderboards extends Controller {

	static public void score(){
		Long leaderboardId = params.get("leaderboardId", Long.class);
		String apiKey = params.get("apiKey");
		Game game = Game.find("byApiKey", apiKey).first();
		if(game==null)
			error("No game matching apikey was found");
		
		Leaderboard leaderboard = Leaderboard.findById(leaderboardId);
		
		if(!leaderboard.game.equals(game))
			error("Leaderboard ("+leaderboard.name+") doesn't match game (" + game.name + ")");
		
		User user = User.findById(params.get("user",Long.class));
		if(!user.authToken.equals(params.get("authToken")))
			error("User authentication failed");
		
		
		Long scoreValue = params.get("score",Long.class);
		
		
		DateTime dateTime = new DateTime();
		
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
		
		
		
		List<Score> scoresList = Score.find("user = ? order by scope",user).fetch();
		Score[] scores = scoresList.toArray(new Score[scoresList.size()]);
		int candidateScope = 1;
		
		for (int i = 0; i < scores.length; i++) {
			Score oldScore = scores[i];
			if(oldScore.scope==1){
				if(score.score > oldScore.score){
					score.scope = 1;
					deleteOldScores(scores, i);
				} else {
					candidateScope++;
				}				
			} else if (oldScore.scope==2){
				if(!(score.year == oldScore.year && score.month == oldScore.month))
					continue;
				
				if(score.score > oldScore.score){
					score.scope = 2;
					deleteOldScores(scores, i);
				} else {
					candidateScope++;
				}
			} else if (oldScore.scope == 3){
				if(!(score.year == oldScore.year && score.month == oldScore.month && score.week == oldScore.week))
					continue;
				
				if(score.score > oldScore.score){
					score.scope = 3;
					deleteOldScores(scores, i);
				} else {
					candidateScope++;
				}
			} else if(oldScore.scope == 4){
				if(!(score.year == oldScore.year && score.month == oldScore.month && score.week == oldScore.week && score.day == oldScore.day))
					continue;
				
				if(score.score > oldScore.score){
					score.scope = 4;
					deleteOldScores(scores, i);
				} else {
					candidateScope++;
				}
			}
		}
		
		if(candidateScope<5){
			score.scope = candidateScope;
			score.save();	
		}
		renderText("Score submitted succesfully");
	}
		
	static private void deleteOldScores(Score[] scores, int from){
		for (int i = from; i < scores.length; i++) {
			scores[i].delete();
		}
	}
	
}
