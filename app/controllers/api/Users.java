package controllers.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import datatransfer.UserDTO;

import models.User;
import play.libs.Crypto;
import play.mvc.Controller;

public class Users extends Controller {

	public static final String PLAYER_GUEST_PREFIX = "Player";

	static public void createGuest() {
		
		long guestNumber = User.getNextGuestNumber();
		
		User user = new User();
		user.userId = guestNumber;
		user.name = PLAYER_GUEST_PREFIX + guestNumber;
		user.privatekey = "privatekey";
		user.guest = true;
		user.save();
		
		UserDTO userDTO = new UserDTO(user.userId, user.name, user.privatekey, user.guest);
		renderJSON(userDTO);
	}
	
	static public void getUserInfo() {
		String userId = params.get("userId");
		if(userId==null || userId.equals(""))
			error(400, "userId is required");
				
		String privatekey = params.get("privatekey");
		
		if(privatekey==null || privatekey.equals(""))
			error(400, "privatekey is required");
		
		User user = User.find("byUserId", userId).first();
		
		if(user==null)
			error(404, "the user " + userId + " doesn't exist");
		
		if(!user.privatekey.equals(privatekey))
			error(401, "error authenticating the user");
		
		UserDTO userDTO = new UserDTO(user.userId, user.name, user.privatekey, user.guest);
		renderJSON(userDTO);
	}
	
	static public void updateUser(){
		String userId = params.get("userId");
		if(userId==null || userId.equals(""))
			error(400, "userId is required");
				
		String privatekey = params.get("privatekey");
		
		if(privatekey==null || privatekey.equals(""))
			error(400, "privatekey is required");
		
		String newName = params.get("newName");
		if(newName.matches("^[ \t]*$"))
			error(400, "new Name must not be empty");
		
		newName = newName.substring(0, Math.min(50,newName.length()));
		
		User user = User.find("byUserId", Long.parseLong(userId)).first();
		
		if(!user.privatekey.equals(privatekey))
			error(401, "error authenticating the user");
		
		user.name = newName;
		user.guest = false;
		
		user.save();	
		UserDTO userDTO = new UserDTO(user.userId, user.name, user.privatekey, user.guest);
		renderJSON(userDTO);
	}
	
}