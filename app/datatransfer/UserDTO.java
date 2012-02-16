package datatransfer;

public class UserDTO {
	public String username;
	public String privatekey;
	public String name;
	public boolean guest;
	
	public UserDTO() {
	}
	
	public UserDTO(String username, String name,String privatekey, boolean guest) {
		this.username = username;
		this.privatekey = privatekey;
		this.name = name;
		this.guest = guest;
	}
	
	
}
