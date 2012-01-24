package controllers.api;

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
		Leaderboard leaderboard = Leaderboard.findById(leaderboardId);
		if(!leaderboard.game.equals(game))
			error("Leaderboard doesn't match game");
		
		User user = User.findById(params.get("user",Long.class));
		if(!user.authToken.equals(params.get("authToken")))
			error("User authentication failed");
		
		
		Long scoreValue = params.get("score",Long.class);
		Score score = new Score();
		score.leaderboard = leaderboard;
		score.score = scoreValue;
		score.user = user;
		score.save();		
		renderText("Score submitted succesfully");
	}
	
}
