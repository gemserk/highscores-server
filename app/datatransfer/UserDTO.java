package datatransfer;

public class UserDTO {
	public long publickey;
	public String privatekey;
	public String name;
	
	public UserDTO() {
	}
	
	public UserDTO(long publickey, String name,String privatekey) {
		this.publickey = publickey;
		this.privatekey = privatekey;
		this.name = name;
	}
}
