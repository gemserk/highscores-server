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
//		Long leaderboardId = params.get("leaderboardId", Long.class);
		String apiKey = params.get("apiKey");
		Game game = Game.find("byApiKey", apiKey).first();
		if(game==null)
			error("No game matching apikey was found");
		
		String leaderboardName = params.get("leaderboard");
		if(leaderboardName==null)
			error("You need to provide the leaderboard name");
		
		Leaderboard leaderboard = Leaderboard.find("byGameAndName",game, leaderboardName).first();
		
		if(leaderboard==null)
			error("Leaderboard ("+ leaderboardName +") doesn't match game (" + game.name + ")");
		
		User user = User.findById(params.get("user",Long.class));
		if(!user.privatekey.equals(params.get("privatekey")))
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
		
		int replacedScores = -1;
		boolean todayScoreExists = false; 
		
		for (int i = 0; i < scores.length; i++) {
			Score oldScore = scores[i];
			todayScoreExists = todayScoreExists || oldScore.day == day;
			if(matchesInScope(oldScore, oldScore)){
				if(score.score > oldScore.score){
					score.scope = oldScore.scope;
					replacedScores = i;
					break;
				} else {
					candidateScope++;
				}	
			}
		}
		
		if(replacedScores!=-1)
			deleteOldScores(scores, replacedScores);
		
		
		
		
		if(replacedScores!=-1 || todayScoreExists == false){
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
		
	static private void deleteOldScores(Score[] scores, int from){
		for (int i = from; i < scores.length; i++) {
			scores[i].delete();
		}
	}
	
}
