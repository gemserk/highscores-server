package controllers.api;

import java.util.UUID;

import models.User;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import play.mvc.Controller;
import play.mvc.With;
import utils.Errors;
import controllers.filters.LogFilter;
import datatransfer.ErrorDTO;
import datatransfer.UserDTO;

@With(LogFilter.class)
public class Users extends Controller {
	static Logger logger = Logger.getLogger(Users.class);
	public static final String PLAYER_GUEST_PREFIX = "Player";

	static public void createGuest() {
		MDC.put(LogFilter.USERCASE, "CREATEGUEST");
		long guestNumber = User.getNextGuestNumber();
		
		User user = new User();
		user.userId = guestNumber;
		user.name = PLAYER_GUEST_PREFIX + guestNumber;
		user.privatekey = UUID.randomUUID().toString();
		user.guest = true;
		user.save();
		
		logger.info(String.format("Creating guest user(%d):%s",user.id,user.name));
		UserDTO userDTO = new UserDTO(user.userId, user.name, user.privatekey, user.guest);
		renderJSON(userDTO);
	}
	
	static public void getUserInfo() {
		MDC.put(LogFilter.USERCASE, "GETUSERINFO");
		String userId = params.get("userId");
		if(userId==null || userId.equals(""))
			renderError(Errors.InvalidUser, "userId is required");
				
		String privatekey = params.get("privatekey");
		
		if(privatekey==null || privatekey.equals(""))
			renderError(Errors.UserAuthenticationFailed, "privatekey is required");
		
		User user = User.find("byUserId", userId).first();
		
		if(user==null)
			renderError(Errors.InvalidUser, "the user " + userId + " doesn't exist");
		
		if(!user.privatekey.equals(privatekey))
			renderError(Errors.UserAuthenticationFailed, "error authenticating the user");
		
		logger.info(String.format("Getting user info from user(%d):%s",user.id,user.name));
		UserDTO userDTO = new UserDTO(user.userId, user.name, user.privatekey, user.guest);
		renderJSON(userDTO);
	}
	
	static public void updateUser(){
		MDC.put(LogFilter.USERCASE, "UPDATEUSER");
		String userId = params.get("userId");
		if(userId==null || userId.equals(""))
			renderError(Errors.InvalidUser, "userId is required");
				
		String privatekey = params.get("privatekey");
		
		if(privatekey==null || privatekey.equals(""))
			renderError(Errors.UserAuthenticationFailed, "privatekey is required");
		
		String newName = params.get("newName");
		if(newName.matches("^[ \t]*$"))
			error(400, "new Name must not be empty");
		
		newName = newName.substring(0, Math.min(50,newName.length()));
		
		User user = User.find("byUserId", Long.parseLong(userId)).first();
		
		if(!user.privatekey.equals(privatekey))
			renderError(Errors.UserAuthenticationFailed, "error authenticating the user");
		
		user.name = newName;
		user.guest = false;
		
		user.save();	
		logger.info(String.format("updating user(%d):%s",user.id,user.name));
		UserDTO userDTO = new UserDTO(user.userId, user.name, user.privatekey, user.guest);
		renderJSON(userDTO);
	}
	
	private static void renderError(Errors error, String message){
		logger.warn(error + " (" + error.errorCode + ") - " + message );
		response.status = Errors.MAINERROR;
		ErrorDTO errorDTO = new ErrorDTO(error.errorCode,message);
		renderJSON(errorDTO);
	}
	
}