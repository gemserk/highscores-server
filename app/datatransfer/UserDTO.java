package datatransfer;

public class UserDTO {
	public long userId;
	public String privatekey;
	public String name;
	
	public UserDTO() {
	}
	
	public UserDTO(long userId, String name,String privatekey) {
		this.userId = userId;
		this.privatekey = privatekey;
		this.name = name;
	}
}
