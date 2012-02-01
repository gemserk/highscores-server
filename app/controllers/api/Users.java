package controllers.api;

import java.util.HashMap;
import java.util.Map;

import datatransfer.UserDTO;

import models.User;
import play.mvc.Controller;

public class Users extends Controller {

	static public void createGuest() {
		User user = new User();
		user.username = "Player" + (int) (Math.random() * 1000);
		user.guest = true;
		user.passwordHash = "password";
		user.privatekey = "privatekey";
		user.save();
		
		UserDTO userDTO = new UserDTO(user.username, user.privatekey);
		renderJSON(userDTO);
	}
	
}