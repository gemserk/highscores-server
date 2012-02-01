package datatransfer;

public class UserDTO {
	public String username;
	public String authToken;
	
	public UserDTO() {
	}
	
	public UserDTO(String username, String authToken) {
		this.username = username;
		this.authToken = authToken;
	}
	
	
}
