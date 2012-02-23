package controllers.api;

import java.util.List;

import models.Game;
import models.Leaderboard;
import models.Score;
import models.User;

import org.joda.time.DateTime;

import play.Play;
import play.mvc.Controller;
import utils.Errors;
import utils.Range;
import controllers.services.LeaderboardService;
import datatransfer.ErrorDTO;
import datatransfer.ScoreDTO;

public class Leaderboards extends Controller {

	

	static public void score() {
		String apiKey = params.get("apiKey");
		Game game = Game.find("byApiKey", apiKey).first();
		if (game == null)
			renderError(Errors.InvalidApiKey,"No game matching apikey was found");

		String leaderboardName = params.get("leaderboard");
		if (leaderboardName == null)
			renderError(Errors.InvalidLeaderboard,"You need to provide the leaderboard name");

		Leaderboard leaderboard = Leaderboard.find("byGameAndName", game, leaderboardName).first();

		if (leaderboard == null)
			renderError(Errors.InvalidLeaderboard,"Leaderboard (" + leaderboardName + ") doesn't match game (" + game.name + ")");

		String userId = params.get("userId");
		if (userId == null)
			renderError(Errors.InvalidUser,"You need to provide the userId of the user");

		User user = User.find("byUserId", Long.parseLong(userId)).first();
		if(user == null)
			renderError(Errors.InvalidUser,"Could not find user with userId" + userId);
		
		if (!user.privatekey.equals(params.get("privatekey")))
			renderError(Errors.UserAuthenticationFailed,"User authentication failed");

		Long scoreValue = params.get("score", Long.class);

		DateTime dateTime = new DateTime();
		String overridendayofyear = params.get("dayofyear");
		if (Play.runingInTestMode() && overridendayofyear != null) {
			System.out.println("Overriding Date to " + overridendayofyear);
			dateTime = dateTime.dayOfYear().setCopy(Integer.parseInt(overridendayofyear));
		}

		LeaderboardService.submitScore(leaderboard, user, scoreValue, dateTime);
		renderText("Score submitted succesfully");
	}


	

	public static void scores() {
		
		System.out.println(params.allSimple());
		
		String apiKey = params.get("apiKey");
		Game game = Game.find("byApiKey", apiKey).first();
		if (game == null)
			renderError(Errors.InvalidApiKey,"No game matching apikey was found");

		String leaderboardName = params.get("leaderboard");
		if (leaderboardName == null)
			renderError(Errors.InvalidLeaderboard,"You need to provide the leaderboard name");

		Leaderboard leaderboard = Leaderboard.find("byGameAndName", game, leaderboardName).first();

		if (leaderboard == null)
			renderError(Errors.InvalidLeaderboard,"Leaderboard (" + leaderboardName + ") doesn't match game (" + game.name + ")");

		String rangeKey = params.get("range");

		Range range = Range.getByKey(rangeKey);
		if (range == null)
			range = Range.All;


		DateTime dateTime = new DateTime();
		String overridendayofyear = params.get("dayofyear");
		if (Play.runingInTestMode() && overridendayofyear != null) {
			System.out.println("Overriding Date to " + overridendayofyear);
			dateTime = dateTime.dayOfYear().setCopy(Integer.parseInt(overridendayofyear));
		}	

		int page = 0;
		int pageSize = 20;
		
		String pageParam = params.get("page");
		if(pageParam!=null && pageParam!="")
			page = Integer.parseInt(pageParam);
		
		String pageSizeParam = params.get("pageSize");
		if(pageSizeParam!=null && pageSizeParam!="")
			pageSize = Integer.parseInt(pageSizeParam);
		
		
		List<Score> scores = LeaderboardService.getScores(leaderboard, range, dateTime, page, pageSize);

		List<ScoreDTO> scoreDTOs = ScoreDTO.convert(scores);
		renderJSON(scoreDTOs);
	}
	
	private static void renderError(Errors error, String message){
		response.status = Errors.MAINERROR;
		ErrorDTO errorDTO = new ErrorDTO(error.errorCode,message);
		renderJSON(errorDTO);
	}
}
