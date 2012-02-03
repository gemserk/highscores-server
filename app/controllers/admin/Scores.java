package controllers.admin;

import java.util.List;

import models.Leaderboard;
import models.User;
import play.mvc.Controller;
import utils.Range;

public class Scores extends Controller{

	static public void score(){
//		List<Game> games = Game.find("select g from Game g, Leaderboard l where l.game = g").fetch();
		List<Leaderboards> leaderboards = Leaderboard.find("select l from Leaderboard l, Game g where l.game = g order by g.name").fetch();
		List<User> users = User.findAll();
		renderArgs.put("leaderboards", leaderboards);
		renderArgs.put("users", users);
		render();
	}
	
	static public void view(){
//		List<Game> games = Game.find("select g from Game g, Leaderboard l where l.game = g").fetch();
		List<Leaderboards> leaderboards = Leaderboard.find("select l from Leaderboard l, Game g where l.game = g order by g.name").fetch();
		List<User> users = User.findAll();
		renderArgs.put("leaderboards", leaderboards);
		renderArgs.put("ranges", Range.values());
		render();
	}
}
