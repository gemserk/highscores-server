package datatransfer;

public class UserDTO {
	public long userId;
	public String privatekey;
	public String name;
	public boolean guest;
	
	public UserDTO() {
	}
	
	public UserDTO(long userId, String name,String privatekey, boolean guest) {
		this.userId = userId;
		this.privatekey = privatekey;
		this.name = name;
		this.guest = guest;
	}
}
