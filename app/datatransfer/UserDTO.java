package datatransfer;

public class UserDTO {
	public String username;
	public String privatekey;
	public String name;
	
	public UserDTO() {
	}
	
	public UserDTO(String username, String name,String privatekey) {
		this.username = username;
		this.privatekey = privatekey;
		this.name = name;
	}
	
	
}
