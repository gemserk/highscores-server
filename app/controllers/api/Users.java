package controllers.api;

import java.util.HashMap;
import java.util.Map;

import models.User;
import play.mvc.Controller;

public class Users extends Controller {

	static public void createGuest() {
		User user = new User();
		user.username = "Player" + (int) (Math.random() * 5);
		user.guest = true;
		user.passwordHash = "password";
		user.authToken = "authToken";
		user.save();
		Map<String, Object> jsonUser = new HashMap<String, Object>();
		jsonUser.put("username", user.username);
		jsonUser.put("authToken", user.authToken);
		renderJSON(jsonUser);
	}
	
}