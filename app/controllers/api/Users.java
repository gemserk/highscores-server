package controllers.api;

import java.util.HashMap;
import java.util.Map;

import datatransfer.UserDTO;

import models.User;
import play.mvc.Controller;

public class Users extends Controller {

	static public void createGuest() {
		
		long guestNumber = User.getNextGuestNumber();
		
		User user = new User();
		user.name = "Player" + guestNumber;
		user.username = user.name.toLowerCase();
		user.guest = true;
		user.passwordHash = "password";
		user.privatekey = "privatekey";
		user.save();
		
		UserDTO userDTO = new UserDTO(user.username, user.name, user.privatekey, true);
		renderJSON(userDTO);
	}
	
	static public void getUserInfo() {
		String username = params.get("username");
		if(username==null || username.equals(""))
			error(400, "username is required");
				
		String privatekey = params.get("privatekey");
		
		if(privatekey==null || privatekey.equals(""))
			error(400, "privatekey is required");
		
		User user = User.find("byUsername", username).first();
		
		if(user==null)
			error(404, "the user " + username + " doesn't exist");
		
		if(!user.privatekey.equals(privatekey))
			error(401, "error authenticating the user");
		
		UserDTO userDTO = new UserDTO(user.username, user.name, user.privatekey, user.guest);
		renderJSON(userDTO);
	}
	
}