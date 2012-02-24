package controllers.forms;

import java.util.List;

import play.mvc.With;

import models.Leaderboard;
import models.User;
import utils.Range;
import controllers.CRUD;
import controllers.admin.Leaderboards;
import controllers.filters.LogFilter;

@With(LogFilter.class)
public class Scores extends CRUD{

	static public void score(){
		List<Leaderboards> leaderboards = Leaderboard.find("select l from Leaderboard l, Game g where l.game = g order by g.name").fetch();
		List<User> users = User.findAll();
		renderArgs.put("leaderboards", leaderboards);
		renderArgs.put("users", users);
		render();
	}
	
	static public void view(){
		List<Leaderboards> leaderboards = Leaderboard.find("select l from Leaderboard l, Game g where l.game = g order by g.name").fetch();
		renderArgs.put("leaderboards", leaderboards);
		renderArgs.put("ranges", Range.values());
		render();
	}
}
