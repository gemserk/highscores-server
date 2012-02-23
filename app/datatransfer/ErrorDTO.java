package datatransfer;

public class ErrorDTO {
	public int errorCode;
	public String message;
	
	public ErrorDTO() {
	}

	public ErrorDTO(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
}
